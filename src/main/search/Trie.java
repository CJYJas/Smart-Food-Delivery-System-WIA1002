package main.search;

import main.model.FoodItem;

// Node for Trie: 26 children for letters a-z, end marker, and optional item.
class TrieNode {
    TrieNode[] children;
    boolean isEndOfWord;
    FoodItem item;
    
    public TrieNode() {
        isEndOfWord = false;
        children = new TrieNode[26];
    }
}

// Trie mapping lowercase food names to FoodItem for fast exact lookups.
// Non-letter characters are ignored by insert/search.
public class Trie {
    private TrieNode root;
    
    public Trie() {
        root = new TrieNode();
    }
    
    // Insert a name and associate it with an item. Skips non-letter chars.
    public void insert(String key, FoodItem item) {
        TrieNode curr = root;
        String word = key.toLowerCase();
        
        for (char c : word.toCharArray()) {
            if (c < 'a' || c > 'z') continue;
            
            int index = c - 'a';
            if (curr.children[index] == null) {
                curr.children[index] = new TrieNode();
            }
            curr = curr.children[index];
        }
        curr.isEndOfWord = true;
        curr.item = item;
    }
    
    // Search for an exact name and return the associated FoodItem, or null.
    public FoodItem search(String key) {
        TrieNode curr = root;
        String word = key.toLowerCase();
        
        for (char c : word.toCharArray())  {
            if (c < 'a' || c > 'z') continue;
            
            int index = c - 'a';
            if (curr.children[index] == null)
                return null;
            curr = curr.children[index];
        }
        return (curr != null && curr.isEndOfWord) ? curr.item : null;
    }
}