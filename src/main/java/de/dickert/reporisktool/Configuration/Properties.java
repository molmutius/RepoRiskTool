package de.dickert.reporisktool.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@ConfigurationProperties("config")
public class Properties
{
    private static final String JSON_OUTPUT_FILE = "repo_risk_tool_filetree.json";

    @Value("${project.name}")
    private String projectName;

    @Value("${project.directory}")
    private String projectDirectory;

    @Value("#{'${project.excludeNames}'.split(',')}")
    private List<String> excludeNames;

    private Path path;

    @Value("${output.directory}")
    private String outputDirectory;

    private Output output;

    private String startDate;
    private String endDate;
    private String branch;
    private int maxDepth;

    public List<String> getExcludeNames()
    {
        return excludeNames;
    }

    public String getProjectDirectory()
    {
        return projectDirectory;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public Path getPath()
    {
        return new File(projectDirectory).toPath();
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public void setProjectDirectory(String projectDirectory)
    {
        this.projectDirectory = projectDirectory;
    }

    public void setExcludeNames(List<String> excludeNames)
    {
        this.excludeNames = excludeNames;
    }

    public void setPath(Path path)
    {
        this.path = path;
    }

    public Output getOutput()
    {
        return new Output();
    }

    public void setOutput(Output output)
    {
        this.output = output;
    }

    public String getJiraUrl()
    {
        throw new RuntimeException("Stub!");
    }

    public String getJiraUser()
    {
        throw new RuntimeException("Stub!");
    }

    public String getJiraPassword()
    {
        throw new RuntimeException("Stub!");
    }

    public class Output
    {
        private Path outputDir;
        private Path outputFile;

        public Path getOutputDir()
        {
            return Paths.get(getOutputDirectory());
        }

        public void setOutputDir(Path outputDir)
        {
            this.outputDir = outputDir;
        }

        public Path getOutputFile()
        {
            return Paths.get(getOutputDirectory(), JSON_OUTPUT_FILE);
        }

        public void setOutputFile(Path outputFile)
        {
            this.outputFile = outputFile;
        }

        private String getOutputDirectory()
        {
            if (outputDirectory.isEmpty())
            {
                return System.getProperty("java.io.tmpdir");
            }
            else
            {
                return outputDirectory;
            }
        }
    }
}
