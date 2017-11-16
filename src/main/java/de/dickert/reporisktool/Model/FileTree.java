package de.dickert.reporisktool.Model;

import de.dickert.reporisktool.Util.NodeVisitor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.io.File;
import java.nio.file.Path;
import java.util.*;

/**
 * <p>
 * A data structure representing the file system structure
 * of the project's git repository at the current head.
 * </p>
 * <p>
 * All nodes are guarenteede to have a custom payload extended from {@link RepoItem}.
 * This might either be {@link RepoFile} or {@link RepoDir}. The
 * custom payload is accessed via {@link DefaultMutableTreeNode#getUserObject()};
 * </p>
 *
 * <p>
 * TODO: Speed up via http://genericgamedev.com/general/using-arrays-to-speed-up-static-tree-traversal/ <br>
 * TODO: consider https://github.com/Scalified/tree or https://stackoverflow.com/questions/18591636/traverse-to-the-deepest-using-java <br>
 * TODO: For displaying consider something simple like https://codepen.io/achudars/pen/cAsEJ or https://github.com/moschan/filetree.css <br>
 * </p>
 */
public class FileTree
{
    private static DefaultMutableTreeNode fileTree;
    private List<String> excludeNames;
    private List<RepoFile> repoFiles;

    public FileTree(String rootPath, List<RepoFile> repoFiles, List<String> excludeNames)
    {
        this.repoFiles = repoFiles;
        this.excludeNames = excludeNames;
        // buildTree() comes last because the assignments above need to happen before
        fileTree = buildTree(new File(rootPath));
    }

    /**
     * Gets the underlying JTree data structure
     * @return {@link MutableTreeNode} fileTree
     */
    public DefaultMutableTreeNode getFileTree()
    {
        return fileTree;
    }

    /**
     * This gets all Issues associated with the given path and all children of that path.
     * The returned list may contain duplicate entries, which is to represent the number
     * of times a single file may have been touched by the same issue several times.
     * @param path The path of the current directory or file
     * @return A flattened list of all issues in this subtree (including provided dir)
     */
    public static List<Issue> getIssuesOfSubtree(Path path)
    {
        final Optional<DefaultMutableTreeNode> subTreeOptional = findNode(path);
        final DefaultMutableTreeNode subTree = subTreeOptional
                .orElseThrow(() -> new IllegalArgumentException(String.format("Couldn't find %s in tree.", path.toString())));
        return getIssuesOfSubtree(subTree);
    }

    public static List<Issue> getIssuesOfSubtree(DefaultMutableTreeNode subTree)
    {
        final List<Issue> issues = new ArrayList<>();
        traverseTree(new NodeVisitor()
        {
            @Override
            public void onFile(RepoFile file)
            {
                issues.addAll(file.getIssues());
            }

            @Override
            public void onDirectory(RepoDir directory) { }

            @Override
            public void onNode(DefaultMutableTreeNode node) { }
        }, subTree);
        return issues;
    }

    private static Optional<DefaultMutableTreeNode> findNode(Path path)
    {
        final Enumeration<DefaultMutableTreeNode> fileTreeEnum = fileTree.preorderEnumeration();
        while (fileTreeEnum.hasMoreElements())
        {
            final DefaultMutableTreeNode currentNode = fileTreeEnum.nextElement();
            final RepoItem currentRepoItem = (RepoItem) currentNode.getUserObject();
            if (currentRepoItem.getPath().equals(path))
            {
                return Optional.of(currentNode);
            }
        }
        return Optional.empty();
    }

    /**
     * Build the tree from the given node onwards. All elements of the tree
     * are visited and get a custom payload subclassed from {@link RepoItem}.
     * @param node The starting node of the file tree
     * @return A fully populated file tree
     */
    private DefaultMutableTreeNode buildTree(File node)
    {
        final DefaultMutableTreeNode tree = new DefaultMutableTreeNode(node.getName());
        // For all files we add information about issues
        if (node.isFile())
        {
            addFileInformation(tree, node);
        }
        // For all directories we add all children
        else if (node.isDirectory())
        {
            addDirectoryInformation(node, tree);
            // This excludes all excluded names from the file array
            final File[] files = node.listFiles(file -> !matchesExcludeName(file));
            if (files != null)
            {
                for (File child: files)
                {
                    tree.add(buildTree(child));
                }
            }
        }
        return tree;
    }

    /**
     * Populate this tree node's user data to new RepoDir object with this dir's path
     */
    private void addDirectoryInformation(File node, DefaultMutableTreeNode tree)
    {
        tree.setUserObject(new RepoDir(node.toPath()));
    }

    /**
     * Add issues to that file in the tree structure if there are any. If not, the node stays untouched.
     * @param node A node of the file tree
     * @param file The file which corresponds to the node in the tree
     */
    private void addFileInformation(DefaultMutableTreeNode node, File file)
    {
        final Path nodePath = file.toPath();
        for (RepoFile repoFile : repoFiles)
        {
            // The node's file path equals the file path of an affected repo file
            if (repoFile.getPath().equals(nodePath))
            {
                // Add node Payload: create new RepoFile, which also holds the file
                node.setUserObject(new RepoFile(file, repoFile.getIssues()));
                return;
            }
        }
        // No known affected file for this node, so we create one with an empty issues list and at this as node payload
        node.setUserObject(new RepoFile(file, Collections.emptyList()));
    }

    /**
     * @param node current node in the directory tree
     * @return true if the node's file name matches any of the excluded names
     */
    private boolean matchesExcludeName(File node)
    {
        return excludeNames.stream().filter(dir -> dir.equals(node.getName())).count() > 0;
    }

    /**
     * Traverse the file tree in preorder from root (the path it was created with)
     * @param nodeVisitor This visitor will be notified on each node
     */
    public void traverseTree(NodeVisitor nodeVisitor)
    {
        traverseTree(nodeVisitor, this.getFileTree());
    }

    /**
     * Traverse the given tree in preorder.
     * @param nodeVisitor This visitor will be notified on each node
     * @param tree the (sub-)tree to traverse
     */
    public static void traverseTree(NodeVisitor nodeVisitor, DefaultMutableTreeNode tree)
    {
        final Enumeration fileTreeEnum = tree.preorderEnumeration();
        while(fileTreeEnum.hasMoreElements())
        {
            final DefaultMutableTreeNode current = (DefaultMutableTreeNode) fileTreeEnum.nextElement();
            final Object currentUserObject = current.getUserObject();
            if (currentUserObject instanceof RepoFile)
            {
                nodeVisitor.onFile((RepoFile) currentUserObject);
                nodeVisitor.onNode(current);
            }
            else if (currentUserObject instanceof RepoDir)
            {
                nodeVisitor.onDirectory((RepoDir) currentUserObject);
                nodeVisitor.onNode(current);
            }
            else
            {
                throw new IllegalStateException(String.format("Found custom data in the tree, which is not of type RepoItem. " +
                        "This should not happen and is most likely a bug. Node: %s", current.toString()));
            }
        }
    }

    /**
     * Utility method to get the children of the given TreeNode
     * @param fileTree Root file node
     * @return a List containing the children of the given tree node
     */
    public static List<DefaultMutableTreeNode> children(DefaultMutableTreeNode fileTree)
    {
        return Collections.list(fileTree.children());
    }

    /**
     * Just prints the tree to sout
     */
    public void dumpTree()
    {
        final Enumeration fileTreeEnum = fileTree.preorderEnumeration();
        while(fileTreeEnum.hasMoreElements())
        {
            DefaultMutableTreeNode current = (DefaultMutableTreeNode) fileTreeEnum.nextElement();
            for (int i = 0; i < current.getLevel(); i++)
            {
                System.out.print(" ");
            }
            System.out.println(current.getLevel() + " " + current.toString());
        }
    }

    /**
     * Overrides standard toString with "beautified" complete file tree output.
     */
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        final Enumeration fileTreeEnum = fileTree.preorderEnumeration();
        while(fileTreeEnum.hasMoreElements())
        {
            DefaultMutableTreeNode current = (DefaultMutableTreeNode) fileTreeEnum.nextElement();
            for (int i = 0; i < current.getLevel(); i++)
            {
                stringBuilder.append(" ");
            }
            stringBuilder.append(current.getLevel()).append(" ").append(current.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public String nodeToString(DefaultMutableTreeNode node)
    {
        return node.toString();
    }
}
