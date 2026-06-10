package main.dataStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom Last-In, First-Out Stack implementation utilizing a single top node reference.
 */
public class MyStack<T> {
    private Node<T> top = null;
    private int size = 0;

    /**
     * Pushes a new element onto the top of the stack.
     */
    public void push(T element) {
        Node<T> newNode = new Node<>(element);
        newNode.next = top;
        top = newNode;
        size++;
    }

    /**
     * Removes and returns the element currently at the top of the stack.
     */
    public T pop() {
        if (isEmpty())
            return null;
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    /**
     * Returns the element currently at the top of the stack without removing it.
     */
    public T peek() {
        if (isEmpty())
            return null;
        return top.data;
    }

    // check the size of the stack
    public int getSize() {
        return size;
    }

    // determine whether the stack is empty
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Converts and reverses the stack elements into an ArrayList representing original insertion order.
     */
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = top;
        // Because a node stack reads from top to bottom (last-in to first-in),
        // we insert at index 0 each time to reverse it back to "first-added first" order.
        while (current != null) {
            list.add(0, current.data);
            current = current.next;
        }
        return list;
    }
}