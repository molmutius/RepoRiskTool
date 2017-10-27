package de.dickert.reporisktool.Util;

import de.dickert.reporisktool.Model.FileTree;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

public class FileToTreeAdderVisitor extends SimpleFileVisitor<Path>
{
    private List<String> excludeDirs = Collections.emptyList();
    private FileTree fileTree;

    public FileToTreeAdderVisitor(List<String> excludeDirs)
    {
        this.excludeDirs = excludeDirs;
    }

    public FileToTreeAdderVisitor()
    {
        // empty constructor
    }

    /**
     * Notified on each file
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes)
    {
        if (attributes.isRegularFile())
        {
            System.out.format("File: %s", file);

        }
        return CONTINUE;
    }

    /**
     * Notified on each directory
     */
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc)
    {
        System.out.format("Directory: %s%n", dir);
        return CONTINUE;
    }

    /**
     * Informs the user about errors when accessing files instead of throwing IOE directly.
     */
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc)
    {
        System.err.println(exc);
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
