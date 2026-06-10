package main.dataStructure;

/**
 * A custom First-In, First-Out Queue implementation utilizing head and tail
 * node references.
 */
public class MyQueue<T> {
    private Node<T> head = null;
    private Node<T> tail = null;
    private int size = 0;

    /**
     * Appends a new element directly to the back of the queue using the tail
     * pointer.
     */
    public void enqueue(T element) {
        Node<T> newNode = new Node<>(element);
        if (tail == null) {
            head = tail = newNode;
            size++;
            return;
        }
        tail.next = newNode;
        tail = newNode;
        size++;
    }

    /**
     * Removes and returns the element at the front of the queue using the head
     * pointer.
     */
    public T dequeue() {
        if (head == null)
            return null;
        T data = head.data;
        head = head.next;
        if (head == null)
            tail = null;
        size--;
        return data;
    }

    /**
     * Returns the element at the front of the queue without removing it.
     */
    public T peek() {
        if (head == null) {
            return null;
        }
        return head.data;
    }

    // check the size of the queue
    public int getSize() {
        return this.size;
    }

    // determine whether the queue is empty
    public boolean isEmpty() {
        return head == null;
    }
}