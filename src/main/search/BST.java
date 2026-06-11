package main.search;

import main.model.FoodItem;

/**
 * Binary search tree storing FoodItems keyed by name (case-insensitive).
 * Does not allow duplicate names.
 */
public class BST {
    /** The root node of the BST. Null if the tree is empty. */
    private TreeNode root;
    
    /**
     * Search for a FoodItem by name in the BST. The search is case-insensitive.
     * 
     * @param targetName the name of the food item to search for
     * @return the matching FoodItem, or null if not found
     */
    public FoodItem search(String targetName) {
        TreeNode current = root;
        while (current != null) {
            if (targetName.compareToIgnoreCase(current.item.getName()) < 0) {
                current = current.left;
            } else if (targetName.compareToIgnoreCase(current.item.getName()) > 0) {
                current = current.right;
            } else {
                return current.item;
            }
        }
        return null;     
    }
    
    /**
     * Insert a FoodItem into the BST. The item is inserted in the correct position based on its name (case-insensitive).
     * If an item with the same name already exists, it will not be inserted again.
     *
     * @param newItem the FoodItem to insert
     * @return true if the item was inserted, false if an item with the same name already exists
     */
    public boolean insert(FoodItem newItem) {
        if (root == null) {
            root = new TreeNode(newItem);
            return true;
            
        } else {
            TreeNode current = root;
            TreeNode parent = null;
            while (current != null) {
                if (newItem.getName().compareToIgnoreCase(current.item.getName()) < 0) {
                    parent = current;
                    current = current.left;
                } else if (newItem.getName().compareToIgnoreCase(current.item.getName()) > 0) {
                    parent = current;
                    current = current.right;
                } else {
                    return false;
                }
            }
            if (newItem.getName().compareToIgnoreCase(parent.item.getName()) < 0) {
                parent.left = new TreeNode(newItem);
            } else {
                parent.right = new TreeNode(newItem);
            } 
            return true;
        }
    }
    
    /** Display the menu items in alphabetical order using an in-order traversal of the BST. */
    public void displayMenu() {
        inOrder(root);
    }
    
    /** Helper method for in-order traversal of the BST. */
    private void inOrder(TreeNode node) {
        if (node != null) {
            inOrder(node.left);
            System.out.println(node.item);
            inOrder(node.right);
        }
    }
}