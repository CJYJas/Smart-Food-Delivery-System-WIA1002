package search;

import model.FoodItem;

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
