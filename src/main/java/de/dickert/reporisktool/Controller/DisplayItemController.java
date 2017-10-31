package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.RepoFile;
import de.dickert.reporisktool.Model.DisplayItem;
import de.dickert.reporisktool.Model.FileTree;

import java.util.List;

public interface DisplayItemController
{
    /**
     * Puts the affected files into "buckets" to display. This method is only based
     * on file system depth. The depth parameter is used to determine the level at
     * which the affected files get grouped together.
     *
     * @param repoFiles a List of {@link RepoFile} as gathered from the git repository
     * @param depth the depth at which items are grouped together
     * @return
     */
    @Deprecated
    List<DisplayItem> getItemsToDisplay(List<RepoFile> repoFiles, int depth);

    /**
     *
     * @param repoFiles
     * @return
     */
    FileTree getFileTree(List<RepoFile> repoFiles);
}
