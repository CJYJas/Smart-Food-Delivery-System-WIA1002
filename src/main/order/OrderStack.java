package main.order;

import java.util.Collections;
import main.model.OrderItem;
import java.util.List;
import main.dataStructure.MyStack;

public class OrderStack {
    private final MyStack<OrderItem> orderItems;

    public OrderStack() {
        orderItems = new MyStack<>();
    }

    public void push(OrderItem item) {
        orderItems.push(item);
    }

    public OrderItem pop() {
        return orderItems.pop();
    }

    public OrderItem peek() {
        return orderItems.peek();
    }

    public int getSize() {
        return orderItems.getSize();
    }

    public boolean isEmpty() {
        return orderItems.isEmpty();
    }

    /** Items in add order (first added first); stack top is last element. */
    public List<OrderItem> getOrderedItems() {
        // Uses the helper method from your custom stack
        return Collections.unmodifiableList(orderItems.toList());
    }
}