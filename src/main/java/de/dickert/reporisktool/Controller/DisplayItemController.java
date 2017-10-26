package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.AffectedFile;
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
     * @param affectedFiles a List of {@link AffectedFile} as gathered from the git repository
     * @param depth the depth at which items are grouped together
     * @return
     */
    @Deprecated
    List<DisplayItem> getItemsToDisplay(List<AffectedFile> affectedFiles, int depth);

    /**
     *
     * @param affectedFiles
     * @return
     */
    FileTree getFileTree(List<AffectedFile> affectedFiles);
}
