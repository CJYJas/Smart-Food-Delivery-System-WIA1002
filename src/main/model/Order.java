package main.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final int orderId;
    private final User user;
    private final Restaurant restaurant;
    private final List<OrderItem> items;
    private String status;

    public Order(int orderId, User user, Restaurant restaurant) {
        this.orderId = orderId;
        this.user = user;
        this.restaurant = restaurant;
        this.items = new ArrayList<>();
        this.status = "Pending";
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
    }

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

    public double getTotalPrice() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

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