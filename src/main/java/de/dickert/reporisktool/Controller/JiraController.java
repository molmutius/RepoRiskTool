package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.Issue;

import java.util.List;

public interface JiraController
{
    /**
     * Returns a list of all {@link Issue}s for the given Project Name
     * @param projectName the name of the JIRA project and ticket number prefix
     * @return a list of all {@link Issue}s
     */
    public List<Issue> getIssues(String projectName);
}
