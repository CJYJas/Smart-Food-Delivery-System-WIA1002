package main.search;

import java.util.List;
import main.model.FoodItem;

// Service that manages search indexes for the menu.
// Maintains a BST for ordered display and a Trie for fast exact-name lookup.
public class SearchService {
    private BST bst;
    private Trie trie;
    
    public SearchService() {
        this.bst = new BST();
        this.trie = new Trie();
    }
    
    // Add an item to both BST and Trie.
    public void addFoodToMenu(FoodItem item) {
        bst.insert(item);
        trie.insert(item.getName(), item);
    }
    
    // Rebuild both indexes from the provided list (replaces existing indexes).
    public void rebuildMenu(List<FoodItem> menuItems) {
        this.bst = new BST();
        this.trie = new Trie();
        for (FoodItem item : menuItems) {
            addFoodToMenu(item);
        }
    }
    
    // Find an item by exact name using the Trie.
    public FoodItem findFood(String name) {
        return trie.search(name);
    }
    
    // Print the menu (alphabetically).
    public void showMenu() {
        System.out.println("--- Available Food Menu ---");
        bst.displayMenu();
    }
}
