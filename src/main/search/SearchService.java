package search;

import model.FoodItem;

public class SearchService {
    private BST bst;
    private Trie trie;
    
    public SearchService() {
        this.bst = new BST();
        this.trie = new Trie();
    }
    
    public void addFoodToMenu(FoodItem item) {
        bst.insert(item);
        trie.insert(item.getName(), item);
    }
    
    public FoodItem findFood(String name) {
        return trie.search(name);
    }
    
    public void showMenu() {
        System.out.println("--- Available Food Menu ---");
        bst.displayMenu();
    }
}
