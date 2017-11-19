package de.dickert.reporisktool.Model;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RepoItem
{
    /** The path of the file or directory */
    @Expose
    Path path;

    @Expose
    private List<Issue> issues;

    public RepoItem(File file)
    {
        this.path = file.toPath();
        this.issues = new ArrayList<>();
    }

    public RepoItem(Path path)
    {
        new RepoItem(path.toFile());
    }

    public RepoItem(File file, List<Issue> issues)
    {
        this.path = file.toPath();
        this.issues = issues;
    }

    public List<Issue> getIssues()
    {
        return issues;
    }

    public void setIssues(List<Issue> issues)
    {
        this.issues = issues;
    }

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

    public File toFile()
    {
        return this.path.toFile();
    }
}
