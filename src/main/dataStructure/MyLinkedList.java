package main.dataStructure;

import java.util.Iterator;

/**
 * A custom implementation of a generic Singly Linked List that implements
 * Iterable.
 */
public class MyLinkedList<T> implements Iterable<T> {
    private Node<T> head = null;
    private int size = 0;

    /**
     * Appends a new item to the end of the linked list by traversing to the last
     * node.
     */
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /**
     * Retrieves the item data located at the specified zero-based index.
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Removes and returns the item located at the specified zero-based index.
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Node<T> removedNode;
        if (index == 0) {
            removedNode = head;
            head = head.next;
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            removedNode = current.next;
            current.next = current.next.next;
        }
        size--;
        return removedNode.data;
    }

    // return size of the list/number of node
    public int getSize() {
        return size;
    }

    // determine whther the list is empty
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns a standard iterator to enable for-each loop traversal over the list
     * items.
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
}