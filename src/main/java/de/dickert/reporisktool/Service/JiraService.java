package de.dickert.reporisktool.Service;

import de.dickert.reporisktool.Model.RepoIssue;

import java.util.List;

public interface JiraService
{
    public List<RepoIssue> getIssues();
}
