package main.search;

import main.model.FoodItem;

// Binary search tree keyed by main.model.FoodItem#getName() (case-insensitive).
// Supports insertion and lookup by name and provides an in-order traversal for printing items in alphabetical order.
public class BST {
    private TreeNode root;
    
    // Search for an item by name (case-insensitive).
    // Returns matching FoodItem or null if not found.
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
    
    // Insert a FoodItem ordered by name.
    // Returns true if inserted, false when duplicate name exists.
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
    
    // Print menu items in alphabetical order (in-order traversal).
    public void displayMenu() {
        inOrder(root);
    }
    
    // In-order traversal helper that prints each item's toString().
    private void inOrder(TreeNode node) {
        if (node != null) {
            inOrder(node.left);
            System.out.println(node.item);
            inOrder(node.right);
        }
    }
}