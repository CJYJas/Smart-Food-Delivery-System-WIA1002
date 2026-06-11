package main.order;

import main.model.OrderItem;
import java.util.List;
import main.dataStructure.MyStack;

/**
 * A stack to manage the current cart items in the order process.
 * Uses a custom MyStack implementation to store OrderItem objects.
 */
public class OrderStack {
    // Stack to hold the items currently in the cart
    private final MyStack<OrderItem> orderItems;

    // Constructor initializes an empty stack for the cart
    public OrderStack() {
        orderItems = new MyStack<>();
    }

    /**
     * Adds an item to the top of the stack.
     *
     * @param item The OrderItem to be added to the stack
    */
    public void push(OrderItem item) {
        orderItems.push(item);
    }

    /**
    * Removes and returns the item at the top of the stack.
    *
    * @return The OrderItem at the top of the stack
    */
    public OrderItem pop() {
        return orderItems.pop();
    }

    /**
     * Returns the item at the top of the stack without removing it.
     *
     * @return The OrderItem at the top of the stack
     */
    public OrderItem peek() {
        return orderItems.peek();
    }

    /**
    * Returns the number of items currently in the stack.
    *
    * @return The number of items in the stack
    */
    public int getSize() {
        return orderItems.getSize();
    }

    /**
     * Checks if the stack is empty.
     *
     * @return true if the stack is empty, false otherwise
     */
    public boolean isEmpty() {
        return orderItems.isEmpty();
    }

    /**
     * Returns an list of the items currently in the stack, in order from bottom to top.
     *
     * @return A list of OrderItem objects in the stack
     */
    public List<OrderItem> getOrderedItems() {
        return orderItems.toList();
    }
}