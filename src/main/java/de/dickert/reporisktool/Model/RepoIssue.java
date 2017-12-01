package de.dickert.reporisktool.Model;

import com.atlassian.jira.rest.client.api.domain.Issue;

/**
 * An RepoIssue is the representation of an RepoIssue as it was found in the Version Control System.
 * This class is intentionally not tightly coupled to any VCS to be extensible in the future. Just
 * create new conversions from the VCS issue to a RepoIssue.
 *
 * To outside users it offers nothing more than the member fields.
 */
public class RepoIssue
{
    private String ticketNumber;
    private String summary;
    private String priority;
    private String type;
    private String iconUrl;
    private String status;

    public RepoIssue(Issue issue)
    {
        this.ticketNumber = issue.getKey();
        this.summary = issue.getSummary();
        this.priority = issue.getPriority() == null ? "" : issue.getPriority().getName();
        this.type = issue.getIssueType().getName();
        this.iconUrl = issue.getIssueType().getIconUri().getPath();
        this.status = issue.getStatus().getDescription();
    }

    public RepoIssue(String ticketNumber)
    {
        this.ticketNumber = ticketNumber;
    }

    public String getTicketNumber()
    {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber)
    {
        this.ticketNumber = ticketNumber;
    }

    public String toString()
    {
        return "RepoIssue: " + ticketNumber;
    }
}
