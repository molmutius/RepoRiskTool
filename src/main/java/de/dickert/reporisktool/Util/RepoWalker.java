package de.dickert.reporisktool.Util;

import de.dickert.reporisktool.Model.TreeNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RepoWalker
{
    private Path root;
    private RepoFileVisitor visitor;

    public RepoWalker(String rootDir, RepoFileVisitor visitor)
    {
        this.root = new File(rootDir).toPath();
        this.visitor = visitor;
    }

    public void walkRepository()
    {
        try
        {
            Files.walkFileTree(root, visitor);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
