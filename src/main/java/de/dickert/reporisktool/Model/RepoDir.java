package de.dickert.reporisktool.Model;

import java.nio.file.Path;

/**
 * Represents a directory of the repository.
 */
public class RepoDir extends RepoItem
{

    public RepoDir(Path path)
    {
        this.path = path;
    }
}
