package main.order;

import main.dataStructure.MyQueue;
import main.model.Order;

public class OrderQueue {
    private final MyQueue<Order> orders;

    public OrderQueue() {
        this.orders = new MyQueue<>();
    }

    public void enqueue(Order order) {
        orders.enqueue(order);
    }

    public Order dequeue() {
        return orders.dequeue();
    }

    public Order peek() {
        return orders.peek();
    }

    public int size() {
        return orders.getSize();
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }
}