package main.dataStructure;

import java.util.Iterator;

public class MyPriorityQueue<T extends Comparable<T>> implements Iterable<T> {
    private Node<T> head = null;
    private int size = 0;

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    // Insert item in the correct sorted position based on priority
    public void offer(T element) {
        Node<T> newNode = new Node<>(element);

        // Case 1: Queue is empty OR new element has higher priority than head
        if (head == null || element.compareTo(head.data) < 0) {
            newNode.next = head;
            head = newNode;
        } else {
            // Case 2: Traverse to find the right spot
            Node<T> current = head;
            while (current.next != null && element.compareTo(current.next.data) >= 0) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    // Remove and return the highest priority item (front of queue)
    public T removeMin() {
        if (isEmpty())
            return null;
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    // Alias for removeMin (like poll in Java)
    public T poll() {
        return removeMin();
    }

    // Look at the highest priority item without removing
    public T peek() {
        if (isEmpty())
            return null;
        return head.data;
    }

    // Clear the entire queue
    public void clear() {
        head = null;
        size = 0;
    }

    // Return number of elements
    public int size() {
        return size;
    }

    // Check if queue is empty
    public boolean isEmpty() {
        return head == null;
    }

    // Check if queue contains a specific element
    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}
