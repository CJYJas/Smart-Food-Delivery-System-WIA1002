package main.dataStructure;

public class Node<T> {
    public T data; // Holds the actual object (like an Order)
    public Node<T> next; // Points to the next node in line

    public Node(T data) {
        this.data = data;
        this.next = null;
    }
}
