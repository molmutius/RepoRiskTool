package de.dickert.reporisktool.Model;

import java.nio.file.Path;
import java.util.List;

/**
 * A DisplayItem represents a visible top level item in the generated report.
 * Think of it as the important unit of information. It is the bucket, holding
 * the information about {@link Issue}s and thus {@link Issue}s associated
 * underneath its {@link #path}.
 */
public class DisplayItem
{
    private Path path;
    private List<Issue> associatedIssues;
}
