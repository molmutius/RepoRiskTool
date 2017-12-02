package de.dickert.reporisktool.Model;

import com.atlassian.jira.rest.client.api.domain.Issue;

/**
 * A RepoIssue is the representation of an issue as it was found in the Ticket system.
 * This class is intentionally not tightly coupled to any system to be extensible in the future. Just
 * create new conversions from the ticket system issue to a RepoIssue.
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
