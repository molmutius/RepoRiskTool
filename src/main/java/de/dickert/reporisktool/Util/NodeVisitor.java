package de.dickert.reporisktool.Util;

import de.dickert.reporisktool.Model.RepoDir;
import de.dickert.reporisktool.Model.RepoFile;

/**
 * This visitor is used when traversing the file tree.
 */
public interface NodeVisitor
{
    /**
     * When traversing, the visitor will be notified for each file
     * @param file The file the visitor will be notified with
     */
    public void onFile(RepoFile file);

    /**
     * When traversing, the visitor will be notified for each directory
     * @param directory The directory the visitor will be notified with
     */
    public void onDirectory(RepoDir directory);
}
