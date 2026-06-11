package main.search;

import main.model.FoodItem;

/**
 * A simple Trie (prefix tree) for storing FoodItems by name. 
 * Only letters 'a' to 'z' are considered; other characters are ignored.
 */
class TrieNode {
    /** Child nodes for each letter 'a' to 'z'. Null if no child for that letter. */
    TrieNode[] children;

    /** True if this node represents the end of a valid key (name). */
    boolean isEndOfWord;

    /** The FoodItem associated with this node if isEndOfWord is true; otherwise null. */
    FoodItem item;
    
    public TrieNode() {
        isEndOfWord = false;
        children = new TrieNode[26];
    }
}

/**
 * A simple Trie (prefix tree) for storing FoodItems by name. 
 * Only letters 'a' to 'z' are considered; other characters are ignored. 
 * Case-insensitive.
 */
public class Trie {
    private TrieNode root;
    
    /** Create an empty Trie. */
    public Trie() {
        root = new TrieNode();
    }
    
    /**
     * Insert a FoodItem into the Trie for the given name.
     * Non-letter characters are ignored; letters are case-insensitive.
     *
     * @param key the name to index
     * @param item the FoodItem to associate
     */
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
    
    /**
     * Search for a FoodItem by exact key (name). 
     * Non-letter characters in the key are ignored, and letters are treated case-insensitively.
     *
     * @param key the name to search for
     * @return the matching FoodItem or null if not found
     */
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