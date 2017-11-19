package de.dickert.reporisktool.Util;

import de.dickert.reporisktool.Model.RepoItem;
import de.dickert.reporisktool.Model.TreeNode;

import java.io.File;
import java.util.List;

public class TreeBuilder
{
    public static TreeNode<RepoItem> buildTree(RepoItem repoItem, List<RepoItem> repoFiles, List<String> excludeNames)
    {
        final TreeNode<RepoItem> treeNode = new TreeNode<>(repoItem);
        final File repoItemFile = repoItem.toFile();
        // For all files we add information about issues
        if (repoItemFile.isFile())
        {
            //addFileInformation(tree, node);
        }
        // For all directories we add all children
        else if (repoItemFile.isDirectory())
        {
            //addDirectoryInformation(node, tree);
            // This excludes all excluded names from the file array
            final File[] files = repoItemFile.listFiles();
            if (files != null)
            {
                for (File child: files)
                {
                    final RepoItem childRepoItem = new RepoItem(child);
                    treeNode.addChild(buildTree(childRepoItem, repoFiles, excludeNames));
                }
            }
        }
        return treeNode;
    }
}
