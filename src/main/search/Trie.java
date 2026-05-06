package search;

import model.FoodItem;

class TrieNode {
    TrieNode[] children;
    boolean isEndOfWord;
    FoodItem item;
    
    public TrieNode() {
        isEndOfWord = false;
        children = new TrieNode[26];
    }
}

public class Trie {
    private TrieNode root;
    
    public Trie() {
        root = new TrieNode();
    }
    
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