package de.dickert.reporisktool.Model;

/**
 * An Issue is the representation of an Issue as it was found in Jira.
 *
 * TODO use https://docs.atlassian.com/jira-rest-java-client-parent/4.0.0/apidocs/com/atlassian/jira/rest/client/api/domain/Issue.html instead
 */
public class Issue
{
    private String ticketNumber;
    private String summary;
    private String priority;
    private String type;
}
