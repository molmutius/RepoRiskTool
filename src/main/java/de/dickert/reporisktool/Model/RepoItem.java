package de.dickert.reporisktool.Model;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RepoItem
{
    /** The path of the file or directory */
    @Expose
    Path path;

    @Expose
    private List<RepoIssue> repoIssues;

    public RepoItem(File file)
    {
        this.path = file.toPath();
        this.repoIssues = new ArrayList<>();
    }

    public RepoItem(Path path)
    {
        new RepoItem(path.toFile());
    }

    public RepoItem(File file, List<RepoIssue> repoIssues)
    {
        this.path = file.toPath();
        this.repoIssues = repoIssues;
    }

    public List<RepoIssue> getRepoIssues()
    {
        return repoIssues;
    }

    public void setRepoIssues(List<RepoIssue> repoIssues)
    {
        this.repoIssues = repoIssues;
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
