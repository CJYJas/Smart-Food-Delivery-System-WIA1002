package search;

import model.FoodItem;

public class BST {
    private TreeNode root;
    
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
    
    public void displayMenu() {
        inOrder(root);
    }
    
    private void inOrder(TreeNode node) {
        if (node != null) {
            inOrder(node.left);
            System.out.println(node.item);
            inOrder(node.right);
        }
    }
}