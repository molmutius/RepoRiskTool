package de.dickert.reporisktool.Util;

import de.dickert.reporisktool.Model.TreeNode;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static java.nio.file.FileVisitResult.*;

public class RepoFileVisitor extends SimpleFileVisitor<Path>
{
    private List<String> excludeDirs;
    private TreeNode rootNode;

    public RepoFileVisitor(List<String> excludeDirs, TreeNode rootNode)
    {
        this.excludeDirs = excludeDirs;
        this.rootNode = rootNode;
    }

    /**
     * Notified on each file
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes)
    {
        if (attributes.isRegularFile())
        {
            System.out.format("File: %s%n", file);
        }
        return CONTINUE;
    }

    /**
     * Notified after each directory
     */
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e)
    {
        System.out.format("Directory: %s%n", dir);
        return CONTINUE;
    }

    /**
     * Informs the user about errors when accessing files instead of throwing IOE directly.
     */
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e)
    {
        System.err.println(e);
        return CONTINUE;
    }

    /**
     * Skip excluded directories
     */
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes)
    {
        for (String excludeDir : excludeDirs)
        {
            if (dir.getFileName().toString().equals(excludeDir))
            {
                return SKIP_SUBTREE;
            }
        }
        return CONTINUE;
    }
}
