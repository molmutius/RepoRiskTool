package de.dickert.reporisktool.Service;

import com.atlassian.fugue.Iterables;
import de.dickert.reporisktool.Configuration.Properties;
import de.dickert.reporisktool.Model.RepoIssueMap;
import de.dickert.reporisktool.Model.RepoItem;
import de.dickert.reporisktool.Model.RepoItemMap;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.util.io.NullOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles interaction with git.
 *
 * TODO: Although file renames and deletes are tracked they might not be visualized/taken into account correctly.
 */
@Primary
@Service
@Qualifier("GitRepoServiceImpl")
public class GitRepoServiceImpl implements RepoService
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Git git;
    private final Repository fileRepository;
    private final Properties properties;
    private final String gitDirectory;
    private final JiraService jiraService;
    private final RepoIssueMap issues;

    @Autowired
    public GitRepoServiceImpl(final Properties properties, final JiraService jiraService)
    {
        this.jiraService = jiraService;
        // TODO add the actual issues once JIRA integration is finished
//      this.issues = new RepoIssueMap(jiraService.getIssues());
        this.issues = new RepoIssueMap(Collections.emptyList());
        this.properties = properties;
        this.gitDirectory = properties.getGitDirectory();
        try
        {
            git = Git.open(new File(gitDirectory));
            fileRepository = git.getRepository();
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(String.format("Unable to access git repository at %s", gitDirectory), e);
        }
    }

    @Override
    public List<RepoItem> getAffectedItems()
    {
        log.info("Getting all affected repository items");
        final RepoItemMap repoItemMap = new RepoItemMap();
        try
        {
            final Iterable<RevCommit> gitLog = prepareLogCommand().call();
            RevCommit previousCommit = Iterables.first(gitLog).getOrThrow(() -> new IllegalStateException(
                    "No first commit found. Check the provided revisions."));
            for (RevCommit currentCommit : gitLog)
            {
                final List<String> diffEntries = getDiff(previousCommit, currentCommit);
                final List<RepoItem> repoItems = buildRepoItemsFromDiff(currentCommit, diffEntries);
                repoItemMap.putAll(repoItems);
                previousCommit = currentCommit;
                currentCommit.disposeBody();
            }
        }
        catch (GitAPIException | IOException e)
        {
            throw new IllegalArgumentException(String.format("Problem accessing git via JGit API %s", gitDirectory), e);
        }
        return repoItemMap.getAll();
    }

    /**
     * Takes all affected files of a commit and converts this information into a list of repo items,
     * where the returned repo items already contain information about the issues as gathered from
     * the ticket system.
     */
    private List<RepoItem> buildRepoItemsFromDiff(final RevCommit commit, final List<String> affectedFiles)
    {
        log.debug(String.format("Bulding repo item for [%s], %d files", commit.toString(), affectedFiles.size()));
        final List<RepoItem> repoItems = new ArrayList<>();
        final Set<String> ticketNumbers = getTicketNumbers(commit);
        for (String ticketNumber : ticketNumbers)
        {
            affectedFiles.forEach(affectedFile -> repoItems.add(buildRepoItem(ticketNumber, affectedFile)));
        }
        return repoItems;
    }

    /**
     * Takes the ticket number, an affected file and all known issues from the ticket system
     * and merges the information into a RepoItem.
     * @return a new repo item holding all the valuable information about issues
     */
    private RepoItem buildRepoItem(final String ticketNumber, final String affectedFile)
    {
        log.debug(String.format("Building repo item for: %s %s", ticketNumber, affectedFile));
        final RepoItem repoItem = new RepoItem(new File(affectedFile));
        repoItem.addRepoIssue(issues.get(ticketNumber));
        return repoItem;
    }

    /**
     * This limits the amount of revisions which need to be parsed. Revision Start, End
     * and Branch are taken from the application.properties and applied accordingly.
     *
     * As a side effect speed and memory consumption is optimized.
     */
    private LogCommand prepareLogCommand() throws IOException, GitAPIException
    {
        final String refStart = properties.getGitRefStart();
        final String refEnd = properties.getGitRefEnd();
        final String branch = properties.getGitBranch();

        LogCommand logCommand = git.log();

        // if we have a branch specified concentrate on that one
        if (branch != null)
        {
            final ObjectId objectId = fileRepository.resolve(branch);
            if (objectId == null)
            {
                throw new IllegalArgumentException(String.format("Git branch incorrect. Spelling error? %s", branch));
            }
            logCommand = logCommand.add(fileRepository.resolve(branch));
        }
        // else take all branches
        else
        {
            logCommand = logCommand.all();
        }

        // if we have a start add that range
        if (refStart != null)
        {
            final ObjectId from = fileRepository.resolve(refStart);
            logCommand = logCommand.add(from);
            // if we have an end add the complete range
            if (refEnd != null)
            {
                final ObjectId to = fileRepository.resolve(refEnd);
                logCommand = logCommand.addRange(from, to);
            }
        }

        return logCommand;
    }

    /**
     * Returns the files which changed between 2 commits (from, to).
     */
    private List<String> getDiff(final RevCommit from, final RevCommit to) throws IOException
    {
        final AbstractTreeIterator fromTreeIterator = getCanonicalTreeParser(from);
        final AbstractTreeIterator toTreeIterator = getCanonicalTreeParser(to);

        final DiffFormatter diffFormatter = new DiffFormatter(NullOutputStream.INSTANCE);
        diffFormatter.setRepository(git.getRepository());
        final List<DiffEntry> diffEntries = diffFormatter.scan(fromTreeIterator, toTreeIterator);
        diffFormatter.close();

        final List<String> touchedFiles = new ArrayList<>();
        diffEntries.forEach(diffEntry -> touchedFiles.add(diffEntry.getNewPath()));
        return touchedFiles;
    }

    private AbstractTreeIterator getCanonicalTreeParser(final ObjectId commitId) throws IOException
    {
        try (final RevWalk walk = new RevWalk(git.getRepository()))
        {
            final RevCommit commit = walk.parseCommit( commitId );
            final ObjectId treeId = commit.getTree().getId();
            try (final ObjectReader reader = git.getRepository().newObjectReader())
            {
                return new CanonicalTreeParser( null, reader, treeId );
            }
        }
    }

    /**
     * Parses the commit message of the given commit and looks for ticket numbers in square brackets
     * @return All ticket numbers found in square brackets at the beginning of the commit message
     */
    private Set<String> getTicketNumbers(final RevCommit commit)
    {
        final Set<String> ticketNumbers = new HashSet<>();
        final String commitMessage = commit.getFullMessage();
        final Pattern pattern = Pattern.compile(String.format("\\[(%s-[\\d]+)\\]", properties.getProjectName()));
        final Matcher matcher = pattern.matcher(commitMessage);
        while (matcher.find())
        {
            ticketNumbers.add(matcher.group());
        }
        log.debug(String.format("Found ticket numbers: %s", ticketNumbers.toString()));
        return ticketNumbers;
    }
}
