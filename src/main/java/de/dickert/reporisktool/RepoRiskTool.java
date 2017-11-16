package de.dickert.reporisktool;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.dickert.reporisktool.Controller.DisplayItemController;
import de.dickert.reporisktool.Controller.FileTreeController;
import de.dickert.reporisktool.Controller.JiraController;
import de.dickert.reporisktool.Controller.RepoController;
import de.dickert.reporisktool.Model.*;
import de.dickert.reporisktool.Util.RepoFileVisitor;
import de.dickert.reporisktool.Util.RepoWalker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.reflect.generics.tree.Tree;

import java.io.File;
import java.nio.file.Path;
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
        Path repoRootPath = new File(projectDirectory).toPath();
//        List<Issue> issues = jiraController.getIssues(projectName);
//        List<RepoFile> affectedFiles = repoController.getAffectedFiles(projectDirectory, startDate, endDate, branch);
//        FileTree fileTree = fileTreeController.buildFileTree(repoRootPath, affectedFiles, excludeNames);

        TreeNode tree = TreeNode.buildTree(repoRootPath.toFile());
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String treeString = gson.toJson(tree);
        System.out.println(treeString);
    }

    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(RepoRiskTool.class, args);
    }
}

