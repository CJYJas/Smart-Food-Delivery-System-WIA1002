package main.dataStructure;

/**
 * A generic Node class representing a single element in a singly linked structure.
 */
public class Node<T> {
    public T data; // Holds the actual object (like an Order)
    public Node<T> next; // Points to the next node in line

    /**
     * Initializes a new node containing the specified payload data.
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }
}