package main.dataStructure;

public class MyQueue<T> {
    private Node<T> head = null;
    private Node<T> tail = null;
    private int size = 0;

    // Add to the back of the queue
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

    // Remove from the front of the queue
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

    public T peek() {
        if (head == null) {
            return null;
        }
        return head.data;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return head == null;
    }
}