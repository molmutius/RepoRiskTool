package de.dickert.reporisktool.Service;

import de.dickert.reporisktool.Model.FileTree;
import de.dickert.reporisktool.Model.RepoItem;
import de.dickert.reporisktool.Model.TreeNode;

import java.nio.file.Path;
import java.util.List;

public interface FileTreeService
{
    FileTree buildFileTree(Path repoRootPath, List<RepoItem> repoFiles, List<String> excludeNames);

    TreeNode buildTree();

    String getJsonRepresentation(TreeNode tree);
}
