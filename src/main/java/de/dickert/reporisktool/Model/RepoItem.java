package de.dickert.reporisktool.Model;

import java.nio.file.Path;

public abstract class RepoItem
{
    /** The path of the file or directory */
    Path path;

    public Path getPath()
    {
        return path;
    }

    public void setPath(Path path)
    {
        this.path = path;
    }

    public String toString()
    {
        return path.getFileName().toString();
    }
}
