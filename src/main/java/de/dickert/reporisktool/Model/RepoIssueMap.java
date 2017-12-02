package de.dickert.reporisktool.Model;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for easier access to the issues based on ticket numbers.
 */
public class RepoIssueMap
{
    final Map<String, RepoIssue> repoIssueMap;

    /**
     * Internally converts the given list into a map with the ticket number as key and the issue itself as value.
     * @param repoIssueList the list to convert
     */
    public RepoIssueMap(@NotNull final List<RepoIssue> repoIssueList)
    {
        repoIssueMap = repoIssueList.stream().collect(Collectors.toMap(RepoIssue::getTicketNumber, Function.identity()));
    }

    public RepoIssue get(@NotNull final String ticketNumber)
    {
        return repoIssueMap.get(ticketNumber);
    }
}