package main.dataStructure;

import java.util.ArrayList;
import java.util.List;

public class MyStack<T> {
    private Node<T> top = null;
    private int size = 0;

    public void push(T element) {
        Node<T> newNode = new Node<>(element);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty())
            return null;
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty())
            return null;
        return top.data;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return top == null;
    }

    // Helper to get items as a list for your UI/Menus
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = top;
        // Because a node stack reads from top to bottom (last-in to first-in),
        // we insert at index 0 each time to reverse it back to "first-added first"
        // order.
        while (current != null) {
            list.add(0, current.data);
            current = current.next;
        }
        return list;
    }
}
