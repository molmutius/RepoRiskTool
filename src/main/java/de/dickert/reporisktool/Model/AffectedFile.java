package de.dickert.reporisktool.Model;

import java.nio.file.Path;
import java.util.List;

/**
 * An {@link AffectedFile} is a file which has been associated with a git commit
 * and thus is affected by one or more issues.
 * At the level of the commit message the issues are represented
 * by their ticket numbers, that's also how the connection to
 * the real issue is made.
 */
public class AffectedFile
{
    /** The path of the file */
    private Path path;
    /** A list of issues represented by their ticket numbers */
    private List<String> issues;

    public Path getPath()
    {
        return path;
    }

    public void setPath(Path path)
    {
        this.path = path;
    }

    public List<String> getIssues()
    {
        return issues;
    }

    public void setIssues(List<String> issues)
    {
        this.issues = issues;
    }
}
