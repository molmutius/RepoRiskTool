package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.RepoItem;

import java.util.List;

public interface RepoController
{
    /**
     * Parses the git log output and finds all affected files for the given input.
     * @param projectDirectory The project directory containing the .git dir
     * @param startDate Time to start looking at
     * @param endDate Time to end looking at
     * @param branch Branch to look at
     * @return Affected files by all git commits for the given input parameters
     */
    public List<RepoItem> getAffectedFiles(String projectDirectory, String startDate, String endDate, String branch);
}
