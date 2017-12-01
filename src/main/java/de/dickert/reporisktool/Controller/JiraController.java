package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.RepoIssue;

import java.util.List;

public interface JiraController
{
    /**
     * Returns a list of all {@link RepoIssue}s for the given Project Name
     * @param projectName the name of the JIRA project and ticket number prefix
     * @return a list of all {@link RepoIssue}s
     */
    public List<RepoIssue> getIssues(String projectName);
}
