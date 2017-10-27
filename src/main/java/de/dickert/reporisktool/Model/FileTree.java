package de.dickert.reporisktool.Model;

import org.springframework.util.StringUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * A data structure representing the file system structure
 * of the project's git repository at the current head.
 *
 * TODO: consider https://github.com/Scalified/tree or https://stackoverflow.com/questions/18591636/traverse-to-the-deepest-using-java
 *
 * TODO: For displaying consider something simple like https://codepen.io/achudars/pen/cAsEJ or https://github.com/moschan/filetree.css
 */
public class FileTree
{
    private DefaultMutableTreeNode fileTree;
    List<AffectedFile> affectedFiles;

    public FileTree(String rootPath, List<AffectedFile> affectedFiles)
    {
        this.affectedFiles = affectedFiles;
        this.fileTree = build(new File(rootPath));
    }

    /**
     * Gets the underlying data structure
     * @return {@link MutableTreeNode} filetree
     */
    public DefaultMutableTreeNode getFileTree()
    {
        return fileTree;
    }

    /**
     * This gets all Issues associated with the given path and all children of that path.
     * @param path The path of the current directory or file
     * @return A flatted list of all issues in this subtree (inclusive)
     */
    public List<Issue> getIssuesOfSubtree(Path path)
    {
        return Collections.emptyList();
    }

    /**
     * Build the tree from the given node onwards
     * @param node
     * @return
     */
    private DefaultMutableTreeNode build(File node)
    {
        DefaultMutableTreeNode tree = new DefaultMutableTreeNode(node.getName());
        if (node.isDirectory())
        {
            for (File child: node.listFiles())
            {
                // For all files we add information about issues
                if (child.isFile())
                {
                    tree = addIssueInformation(tree, child);
                }
                tree.add(build(child));
            }
        }
        return tree;
    }

    /**
     * Add issues to that file in the tree structure if there are any. If no the node stays untouched.
     * @param node
     * @param file
     * @return A new node containing information about associated issues if there are any
     */
    private DefaultMutableTreeNode addIssueInformation(DefaultMutableTreeNode node, File file)
    {
        final Path nodePath = file.toPath();
        for (AffectedFile affectedFile : affectedFiles)
        {
            if (affectedFile.getPath().equals(nodePath))
            {
                node.setUserObject(affectedFile.getIssues());
            }
        }
        return node;
    }

    public void dumpTree()
    {
        Enumeration fileTreeEnum = fileTree.depthFirstEnumeration();
        while(fileTreeEnum.hasMoreElements())
        {
            DefaultMutableTreeNode current = (DefaultMutableTreeNode) fileTreeEnum.nextElement();
            for (int i = 0; i < current.getDepth(); i++)
            {
                System.out.print(" ");
            }
            System.out.println(current.toString());
        }
    }
}
