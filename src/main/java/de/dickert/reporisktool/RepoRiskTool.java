package de.dickert.reporisktool;

import de.dickert.reporisktool.Controller.DisplayItemController;
import de.dickert.reporisktool.Controller.JiraController;
import de.dickert.reporisktool.Controller.RepoController;
import de.dickert.reporisktool.Model.AffectedFile;
import de.dickert.reporisktool.Model.DisplayItem;
import de.dickert.reporisktool.Model.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Value("${project.name}")
    private String projectName;

    private String projectDirectory;
    private String startDate;
    private String endDate;
    private String branch;

    private int maxDepth;

    @Override
    public void run(String... args) throws Exception
    {
        List<Issue> issues = jiraController.getIssues(projectName);
        List<AffectedFile> affectedFiles = repoController.getAffectedFiles(projectDirectory, startDate, endDate, branch);
        List<DisplayItem> displayItems = displayItemController.getItemsToDisplay(affectedFiles, maxDepth);
    }

    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(RepoRiskTool.class, args);
    }
}
