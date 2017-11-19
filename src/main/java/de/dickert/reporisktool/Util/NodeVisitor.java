package de.dickert.reporisktool.Util;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This visitor is used when traversing the file tree.
 */
public interface NodeVisitor
{
    /**
     * When traversing, the visitor will be notified for each node with the current node
     * @param node The current node
     */
    public void onNode(DefaultMutableTreeNode node);
}
