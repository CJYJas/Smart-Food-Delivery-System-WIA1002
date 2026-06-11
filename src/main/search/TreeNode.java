package main.search;

import main.model.FoodItem;

/**
 * Node used by the BST to store a FoodItem and links to child nodes.
 *
 * Package-private helper for the tree implementation; stores the item and
 * references to left/right children.
 */
class TreeNode {
    /** The FoodItem stored at this node. */
    FoodItem item;

    /** Left child (may be null). */
    TreeNode left;

    /** Right child (may be null). */
    TreeNode right;

    /**
     * Construct a new TreeNode with the given FoodItem and no children.
     * 
     * @param item the FoodItem to store at this node
     */
    public TreeNode(FoodItem item) {
        this.item = item;
        this.left = null;
        this.right = null;
    }
}
