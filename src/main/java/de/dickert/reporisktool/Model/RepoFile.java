package de.dickert.reporisktool.Model;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An {@link RepoFile} is a file which of the repository which might have been
 * associated with a git commit and thus is affected by one or more issues.
 * At the level of the commit message the issues are represented by their
 * ticket numbers, that's also how the connection to the real issue is made.
 * In case this file is not affeced by any git commit, it has an empty
 * issues list.
 */
public class RepoFile extends RepoItem
{
    /** A list of issues represented by their ticket numbers */
    private List<Issue> issues;

    public RepoFile(File file)
    {
        this.issues = new ArrayList<Issue>();
        this.path = file.toPath();
    }

    public RepoFile(File file, List<Issue> issues)
    {
        this.issues = issues;
        this.path = file.toPath();
    }

    public List<Issue> getIssues()
    {
        return issues;
    }

    public void setIssues(List<Issue> issues)
    {
        this.issues = issues;
    }
}
