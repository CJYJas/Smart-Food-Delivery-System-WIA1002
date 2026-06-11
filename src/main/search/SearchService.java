package main.search;

import java.util.List;
import main.model.FoodItem;

/**
 * Manages BST and Trie search indexes for menu items.
 */
public class SearchService {
    private BST bst;
    private Trie trie;
    
    /** Construct a new SearchService with empty BST and Trie indexes. */
    public SearchService() {
        this.bst = new BST();
        this.trie = new Trie();
    }
    
    /**
     * Add a FoodItem to both the BST and Trie indexes.
     *
     * @param item the FoodItem to add to the menu
     */
    public void addFoodToMenu(FoodItem item) {
        bst.insert(item);
        trie.insert(item.getName(), item);
    }
    
    /**
     * Rebuild BST and Trie indexes from the provided list.
     *
     * @param menuItems items to populate the indexes
     */
    public void rebuildMenu(List<FoodItem> menuItems) {
        this.bst = new BST();
        this.trie = new Trie();
        for (FoodItem item : menuItems) {
            addFoodToMenu(item);
        }
    }
    
    /**
     * Search for a FoodItem by name using the Trie index. 
     * The search is case-insensitive, and non-letter characters in the name are ignored.
     * 
     * @param name the name of the food item to search for
     * @return the matching FoodItem, or null if not found
     */
    public FoodItem findFood(String name) {
        return trie.search(name);
    }
    
    /** Display the menu items in alphabetical order using an in-order traversal of the BST. */
    public void showMenu() {
        System.out.println("--- Available Food Menu ---");
        bst.displayMenu();
    }
}
