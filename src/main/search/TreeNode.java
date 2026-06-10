package main.search;

import main.model.FoodItem;

// Tree node used by the BST.
// Stores a FoodItem and links to left/right child nodes.
class TreeNode {
    FoodItem item;
    TreeNode left;
    TreeNode right;

    public TreeNode(FoodItem item) {
        this.item = item;
        this.left = null;
        this.right = null;
    }
}
