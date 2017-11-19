package de.dickert.reporisktool.Model;

import com.google.common.collect.TreeTraverser;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> implements Iterable<TreeNode<T>>
{
    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("node")
    @Expose
    private T data;

    private TreeNode<T> parent;

    @SerializedName("nodes")
    @Expose
    private List<TreeNode<T>> children;

    public T getData()
    {
        return this.data;
    }

    public boolean isRoot()
    {
        return parent == null;
    }

    public boolean isLeaf()
    {
        return children == null || children.size() == 0;
    }

    public List<TreeNode<T>> children()
    {
        return this.children;
    }

    private List<TreeNode<T>> elementsIndex;

    public TreeNode(T data)
    {
        this.text = data.toString();
        this.data = data;
        this.elementsIndex = new LinkedList<>();
        this.elementsIndex.add(this);
    }

    public TreeNode<T> addChild(TreeNode<T> childNode)
    {
        childNode.parent = this;
        // We don't initialize children to something because GSON is conveniently
        // ignoring null fields and we want to make use of that behaviour.
        if (this.children == null)
        {
            this.children = new LinkedList<>();
        }
        this.children.add(childNode);
        this.registerChildForSearch(childNode);
        return childNode;
    }

    public int getLevel()
    {
        if (this.isRoot())
        {
            return 0;
        }
        else
        {
            return parent.getLevel() + 1;
        }
    }

    private void registerChildForSearch(TreeNode<T> node)
    {
        elementsIndex.add(node);
        if (parent != null)
        {
            parent.registerChildForSearch(node);
        }
    }

    public TreeNode<T> findTreeNode(Comparable<T> cmp)
    {
        for (TreeNode<T> element : this.elementsIndex)
        {
            T elData = element.data;
            if (cmp.compareTo(elData) == 0)
            {
                return element;
            }
        }

        return null;
    }

    @Override
    public String toString()
    {
        return data != null ? data.toString() : "[data null]";
    }

    @Override
    public Iterator<TreeNode<T>> iterator()
    {
        TreeNodeIter<T> iter = new TreeNodeIter<T>(this);
        return iter;
    }

    public void dumpTree()
    {
        TreeTraverser<TreeNode<T>> traverser = new TreeTraverser<TreeNode<T>>() {
            @Override
            public Iterable<TreeNode> children(TreeNode root) {
                return root.children();
            }
        };

        for (TreeNode currentNode : traverser.preOrderTraversal(this))
        {
            System.out.println(currentNode.getLevel() + " " + currentNode.toString());
        }
    }
}