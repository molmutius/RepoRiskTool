package de.dickert.reporisktool.Controller;

import de.dickert.reporisktool.Model.RepoFile;
import de.dickert.reporisktool.Model.FileTree;

import java.nio.file.Path;
import java.util.List;

public interface FileTreeController
{
    public FileTree buildFileTree(Path root, List<RepoFile> repoFiles, List<String> excludeNames);
}
