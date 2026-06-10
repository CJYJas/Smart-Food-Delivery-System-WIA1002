package main.dataStructure;

import java.util.Iterator;

/**
 * A custom implementation of a Priority Queue based on a sequentially sorted
 * linked list.
 */
public class MyPriorityQueue<T extends Comparable<T>> implements Iterable<T> {
    private Node<T> head = null;
    private int size = 0;

    /**
     * Returns an iterator that traverses the queue elements from highest to lowest
     * priority.
     */
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

    /**
     * Inserts an item into its correct sorted position based on natural priority
     * order.
     */
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

    /**
     * Removes and returns the highest priority item from the front of the queue.
     */
    public T removeMin() {
        if (isEmpty())
            return null;
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    /**
     * Serves as an alias for removeMin to match standard queue naming conventions.
     */
    public T poll() {
        return removeMin();
    }

    /**
     * Returns the highest priority item from the front without removing it.
     */
    public T peek() {
        if (isEmpty())
            return null;
        return head.data;
    }

    /**
     * Resets the queue by clearing the head reference and setting the size to zero.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    // check the size of the priority queue
    public int size() {
        return size;
    }

    // check whether the priority queue is empty
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Checks if a matching element exists within the queue by performing a linear
     * search.
     */
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