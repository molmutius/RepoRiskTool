package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.FileTree;
import de.dickert.reporisktool.Model.RepoItem;
import de.dickert.reporisktool.Model.TreeNode;

import java.nio.file.Path;
import java.util.List;

public interface FileTreeController
{
    FileTree buildFileTree(Path repoRootPath, List<RepoItem> repoFiles, List<String> excludeNames);

    TreeNode buildTree();

    String getJsonRepresentation(TreeNode tree);
}
