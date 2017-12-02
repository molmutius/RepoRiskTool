package de.dickert.reporisktool.Service;

import de.dickert.reporisktool.Model.RepoItem;

import java.util.List;
import java.util.Set;

public interface RepoService
{
    /**
     * Walks over the VCS and collects all files which have been affected
     * and also links them to commits/entries into that VCS, where the
     * filename is the key and the value are the RepoIsseus as collected
     * by a ticket system like JIRA.
     *
     * @return A list of RepoItems
     */
    public List<RepoItem> getAffectedItems();
}
