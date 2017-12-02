package de.dickert.reporisktool.Configuration;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Component
@ConfigurationProperties("config")
public class Properties
{
    private static final String JSON_OUTPUT_FILE = "repo_risk_tool_filetree.json";

    @Value("${project.name}")
    private String projectName;

    @Value("${project.git.ref.from}")
    private String gitRefStart;

    @Value("${project.git.ref.to}")
    private String gitRefEnd;

    @Value("${project.git.directory}")
    private String gitDirectory;

    @Value("${project.git.branch}")
    private String gitBranch;

    @Value("${project.directory}")
    private String projectDirectory;

    @Value("#{'${project.excludeNames}'.split(',')}")
    private List<String> excludeNames;

    private Path path;

    @Value("${output.directory}")
    private String outputDirectory;

    @Value("${jira.url}")
    private String jiraUrl;

    @Value("${jira.user}")
    private String jiraUser;

    @Value("${jira.password}")
    private String jiraPassword;

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

    public Path getProjectPath()
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
        return Optional.of(jiraUrl).orElseThrow(() -> new IllegalArgumentException("No JIRA URL provided"));
    }

    public String getJiraUser()
    {
        return Optional.of(jiraUser).orElseThrow(() -> new IllegalArgumentException("No JIRA user provided"));
    }

    public String getJiraPassword()
    {
        return Optional.of(jiraPassword).orElseThrow(() -> new IllegalArgumentException("No JIRA password provided"));
    }

    /**
     * @return Git ref start from application.properties or null
     */
    @Nullable
    public String getGitRefStart()
    {
        return Strings.isNullOrEmpty(gitRefStart) ? null : gitRefStart;
    }

    /**
     * @return Git ref end from application.properties or null
     */
    @Nullable
    public String getGitRefEnd()
    {
        return Strings.isNullOrEmpty(gitRefEnd) ? null : gitRefEnd;
    }

    @Nullable
    public String getGitBranch()
    {
        return Strings.isNullOrEmpty(gitBranch) ? null : gitBranch;
    }

    public String getGitDirectory()
    {
        return gitDirectory;
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
