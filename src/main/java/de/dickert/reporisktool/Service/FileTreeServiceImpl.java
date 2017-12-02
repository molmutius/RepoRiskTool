package de.dickert.reporisktool.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.dickert.reporisktool.Configuration.Properties;
import de.dickert.reporisktool.Model.FileTree;
import de.dickert.reporisktool.Model.RepoItem;
import de.dickert.reporisktool.Model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Primary
@Controller
@Qualifier("FileTreeServiceImpl")
public class FileTreeServiceImpl implements FileTreeService
{
    private static final Logger log = LoggerFactory.getLogger(FileTreeServiceImpl.class);

    private Properties config;
    private final Path rootPath;
    private final List<String> excludes;

    @Autowired
    public FileTreeServiceImpl(Properties config)
    {
        this.rootPath = config.getProjectPath();
        this.excludes = config.getExcludeNames();
        this.config = config;
    }

    @Override
    public FileTree buildFileTree(Path root, List<RepoItem> repoFiles, List<String> excludeNames)
    {
        return new FileTree(root.toString(), repoFiles, excludeNames);
    }

    @Override
    public TreeNode buildTree()
    {
        log.debug("Building file tree structure for rootpath: {}", rootPath);
        final TreeNode tree = buildTree(new RepoItem(rootPath.toFile()));
        return tree;
    }

    private TreeNode<RepoItem> buildTree(RepoItem repoItem)
    {
        final TreeNode<RepoItem> treeNode = new TreeNode<>(repoItem);
        final File repoItemFile = repoItem.toFile();
        // For all files we add information about issues
        if (repoItemFile.isFile())
        {
            //addFileInformation(tree, node);
        }
        // For all directories we add all children
        else if (repoItemFile.isDirectory())
        {
            //addDirectoryInformation(node, tree);
            // This excludes all excluded names from the file array
            final File[] files = repoItemFile.listFiles(file -> !matchesExcludeName(file));
            if (files != null)
            {
                for (File child: files)
                {
                    final RepoItem childRepoItem = new RepoItem(child);
                    treeNode.addChild(buildTree(childRepoItem));
                }
            }
        }
        return treeNode;
    }

    public String getJsonRepresentation(TreeNode tree)
    {
        final Path outputLocation = config.getOutput().getOutputFile();
        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        final Type type = new TypeToken<TreeNode<RepoItem>>() {}.getType();
        final String jsonTree = gson.toJson(tree, type);
        try
        {
            log.debug("Writing File Tree json file to '{}'", outputLocation);
            Files.write(outputLocation, jsonTree.getBytes());
        }
        catch (IOException e)
        {
            log.error("Failed to write json file", e);
        }
        return "[" + jsonTree + "]";
    }

    /**
     * @param node current node in the directory tree
     * @return true if the node's file name matches any of the excluded names
     */
    private boolean matchesExcludeName(File node)
    {
        return excludes.stream().filter(dir -> dir.equals(node.getName())).count() > 0;
    }
}
