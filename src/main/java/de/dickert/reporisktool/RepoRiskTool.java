package de.dickert.reporisktool;

import de.dickert.reporisktool.Controller.DisplayItemController;
import de.dickert.reporisktool.Controller.FileTreeController;
import de.dickert.reporisktool.Controller.JiraController;
import de.dickert.reporisktool.Controller.RepoController;
import de.dickert.reporisktool.Model.RepoFile;
import de.dickert.reporisktool.Model.FileTree;
import de.dickert.reporisktool.Model.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.List;

@SpringBootApplication
public class RepoRiskTool implements CommandLineRunner
{
    @Autowired
    private JiraController jiraController;

    @Autowired
    private RepoController repoController;

    @Autowired
    private DisplayItemController displayItemController;

    @Autowired
    private FileTreeController fileTreeController;

    @Value("${project.name}")
    private String projectName;

    @Value("${project.directory}")
    private String projectDirectory;

    @Value("#{'${project.excludeNames}'.split(',')}")
    private List<String> excludeNames;

    private String startDate;
    private String endDate;
    private String branch;

    private int maxDepth;

    @Override
    public void run(String... args) throws Exception
    {
        List<Issue> issues = jiraController.getIssues(projectName);
        List<RepoFile> affectedFiles = repoController.getAffectedFiles(projectDirectory, startDate, endDate, branch);
        //List<DisplayItem> displayItems = displayItemController.getItemsToDisplay(repoFiles, maxDepth);
        FileTree fileTree = fileTreeController.buildFileTree(new File(projectDirectory).toPath(), affectedFiles, excludeNames);
    }

    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(RepoRiskTool.class, args);
    }
}

