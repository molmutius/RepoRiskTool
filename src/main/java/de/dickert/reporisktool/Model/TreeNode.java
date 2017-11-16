package de.dickert.reporisktool.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Custom implementation of a tree.
 *
 * At the moment it does not provide more benefit then jtree.
 */
public class TreeNode implements Iterable<TreeNode>
{
    @SerializedName("node")
    @Expose
    private File data;

    @SerializedName("payload")
    @Expose
    private RepoItem payload;

    private TreeNode parent;

    @SerializedName("children")
    @Expose
    private List<TreeNode> children;

    private boolean isRoot()
    {
        return parent == null;
    }

    public boolean isLeaf()
    {
        return children.size() == 0;
    }

    private List<TreeNode> elementsIndex;

    public RepoItem getPayload()
    {
        return payload;
    }

    public void setPayload(RepoItem payload)
    {
        this.payload = payload;
    }

    public TreeNode(File data)
    {
        this.data = data;
        this.children = new LinkedList<TreeNode>();
        this.elementsIndex = new LinkedList<TreeNode>();
        this.elementsIndex.add(this);
    }

    public TreeNode addChild(TreeNode child)
    {
        TreeNode childNode = new TreeNode(child.data);
        childNode.parent = this;
        this.children.add(childNode);
        this.registerChildForSearch(childNode);
        return childNode;
    }

    public int getLevel()
    {
        if (this.isRoot())
            return 0;
        else
            return parent.getLevel() + 1;
    }

    private void registerChildForSearch(TreeNode node)
    {
        elementsIndex.add(node);
        if (parent != null)
            parent.registerChildForSearch(node);
    }

    public TreeNode findTreeNode(Comparable<Object> cmp)
    {
        for (TreeNode element : this.elementsIndex)
        {
            Object elData = element.data;
            if (cmp.compareTo(elData) == 0)
                return element;
        }

        return null;
    }

    @Override
    public String toString()
    {
        return data != null ? data.toString() : "[data null]";
    }

    @Override
    public Iterator<TreeNode> iterator()
    {
        TreeNodeIter<Object> iter = new TreeNodeIter<Object>(this);
        return iter;
    }

    public List<TreeNode> children()
    {
        return this.children;
    }

    public static TreeNode buildTree(File node)
    {
        final TreeNode tree = new TreeNode(node);
        // For all files we add information about issues
        if (node.isFile())
        {
            //addFileInformation(tree, node);
            System.out.println(tree.getLevel());
            System.out.println(node.getName());
        }
        // For all directories we add all children
        else if (node.isDirectory())
        {
            System.out.println(tree.getLevel());
            System.out.println(node.getName());
            //addDirectoryInformation(node, tree);
            // This excludes all excluded names from the file array
            final File[] files = node.listFiles();
            if (files != null)
            {
                for (File child: files)
                {
                    tree.addChild(buildTree(child));
                }
            }
        }
        return tree;
    }
}