package order;

import java.util.LinkedList;
import model.*;

public class OrderQueue {
    private LinkedList<Order> orders;

    public OrderQueue() {
        this.orders = new LinkedList<>();
    }

    public void enqueue(Order order) {
        orders.addLast(order);
    }

    public Order dequeue() {
        return orders.removeFirst();
    }
    
    public Order peek() {
        return orders.peekFirst();
    }

    public int size() {
        return orders.size();
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }
}