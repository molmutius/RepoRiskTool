package de.dickert.reporisktool.Model;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for handling all repo items in a map.
 */
public class RepoItemMap
{
    private Map<Path, RepoItem> repoItemMap;

    public RepoItemMap()
    {
        repoItemMap = new HashMap<>();
    }

    /**
     * Puts the new repoItem into the map if it does not exist, yet. If it
     * already exists it's merged with the already existing one.
     * @param repoItem The item to put to the map
     */
    private void put(final RepoItem repoItem)
    {
        final RepoItem existingItem = repoItemMap.get(repoItem.getPath());
        if (existingItem != null)
        {
            existingItem.addRepoIssues(repoItem.getRepoIssues());
            repoItemMap.put(existingItem.getPath(), existingItem);
        }
        else
        {
            repoItemMap.put(repoItem.getPath(), repoItem);
        }
    }

    /**
     * Puts all repo items into the map. If repoItems is empty, nothing happen
     * @param repoItems the list of items to put into the map, may be empty
     */
    public void putAll(@NotNull final List<RepoItem> repoItems)
    {
        repoItems.forEach(this::put);
    }

    /**
     * @return All of this map's values as list
     */
    public List<RepoItem> getAll()
    {
        return new ArrayList<>(repoItemMap.values());
    }
}
