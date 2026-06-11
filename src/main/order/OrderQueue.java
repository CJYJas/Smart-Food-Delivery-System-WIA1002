package main.order;

import main.dataStructure.MyQueue;
import main.model.Order;

/**
 * Manages confirmed orders using a Queue data structure.
 * Implements First-In-First-Out (FIFO) processing,
 * ensuring orders are handled in the same sequence they are received.
 */
public class OrderQueue {

    // Custom queue storing pending orders
    private final MyQueue<Order> orders;

    /**
     * Creates an empty order queue.
     */
    public OrderQueue() {
        this.orders = new MyQueue<>();
    }

    /**
     * Adds a confirmed order to the rear of the queue.
     *
     * @param order Order to be added
     */
    public void enqueue(Order order) {
        orders.enqueue(order);
    }

    /**
     * Removes and returns the order at the front of the queue.
     *
     * @return The order at the front of the queue
     */
    public Order dequeue() {
        return orders.dequeue();
    }

    /**
     * Returns the order at the front of the queue without removing it.
     *
     * @return The order at the front of the queue
     */
    public Order peek() {
        return orders.peek();
    }

    /**
     * Returns the number of orders in the queue.
     *
     * @return The number of orders in the queue
     */
    public int size() {
        return orders.getSize();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return orders.isEmpty();
    }
}