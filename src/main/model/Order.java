package main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer order.
 * Each order belongs to a specific user and restaurant and contains a collection of ordered food items.
 */

public class Order {
    // Unique identifier for the order
    private final int orderId;

    // Customer who placed the order
    private final User user;

    // Restaurant receiving the order
    private final Restaurant restaurant;

    // Stores all items included in this order
    private final List<OrderItem> items;

    // Current order status (e.g., Pending, Processing, Completed)
    private String status;

    /**
     * Constructs a new Order object.
     *
     * @param orderId Unique order ID
     * @param user Customer who places the order
     * @param restaurant Restaurant receiving the order
     */
    public Order(int orderId, User user, Restaurant restaurant) {
        this.orderId = orderId;
        this.user = user;
        this.restaurant = restaurant;
        this.items = new ArrayList<>();
        this.status = "Pending";
    }

    /**
     * Adds an item to the order.
     *
     * @param item OrderItem to be added
     */
    public void addItem(OrderItem item) {
        items.add(item);
    }

    /**
     * Removes an item from the order.
     *
     * @param item OrderItem to be removed
     */
    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public User getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Calculates the total price of all items in the order.
     *
     * @return Total price of all items
     */
    public double getTotalPrice() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    /**
     * Returns a string representation of the order.
     *
     * @return String representation of the order
     */
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", user=" + user.getUsername() +
                ", restaurant=" + restaurant.getName() +
                ", items=" + items.size() +
                ", status='" + status + '\'' +
                '}';
    }
}