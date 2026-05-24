package main.order;

import java.util.ArrayList;
import java.util.List;
import main.model.Order;
import main.model.OrderItem;
import main.model.Restaurant;
import main.model.User;

public class OrderService {
    private OrderStack cartStack;
    private final OrderQueue processingQueue;
    private final List<Order> orderHistory;

    public OrderService() {
        this.cartStack = new OrderStack(); 
        this.processingQueue = new OrderQueue();
        this.orderHistory = new ArrayList<>();
    }

    public void createNewOrder(User user, Restaurant restaurant) {
        cartStack = new OrderStack();
    }

    /** Restaurant id from the first line in the cart, or null if the cart is empty. */
    public String getCartRestaurantId() {
        if (cartStack.isEmpty()) {
            return null;
        }
        return cartStack.getOrderedItems().get(0).getFoodItem().getRestaurantID();
    }

    public void addItemToCart(OrderItem item) {
        cartStack.push(item);
        System.out.println("  Item added: " + item.getFoodItem().getName());
    }

    public void undoLastItem() {
        if (!cartStack.isEmpty()) {
            OrderItem removed = cartStack.pop();
            System.out.println("  Successfully undo " + removed.getFoodItem().getName() + ".");
        } else {
            System.out.println("  Cart is empty, nothing to undo.");
        }
    }

    public void confirmOrder(int orderId, User user, Restaurant restaurant) {
        if (cartStack.isEmpty()) {
            System.out.println("  Cannot place order: Cart is empty.");
            return;
        }

        Order newOrder = new Order(orderId, user, restaurant);
        while (!cartStack.isEmpty()) {
            newOrder.addItem(cartStack.pop());
        }
        processingQueue.enqueue(newOrder);
        orderHistory.add(newOrder);
        System.out.println("  Order #" + orderId + " placed successfully!");
    }

    public void printCartOverview(List<Restaurant> restaurants) {
        if (cartStack.isEmpty()) {
            System.out.println("  Cart is empty.");
            return;
        }
        String rid = getCartRestaurantId();
        for (Restaurant r : restaurants) {
            if (r.getRestaurantID().equals(rid)) {
                System.out.println("  Restaurant: " + r.getName());
                break;
            }
        }
        int n = 1;
        for (OrderItem oi : cartStack.getOrderedItems()) {
            System.out.printf("  %d. %10s   x %d    RM %.2f%n",
                n++, oi.getFoodItem().getName(), oi.getQuantity(), oi.getTotalPrice());
        }
    }

    public double getCartTotal() {
        double t = 0;
        for (OrderItem oi : cartStack.getOrderedItems()) {
            t += oi.getTotalPrice();
        }
        return t;
    }

    public boolean isCartEmpty() {
        return cartStack.isEmpty();
    }

    public void processNextOrder() {
        if (processingQueue.isEmpty()) {
            System.out.println("  No pending orders in the queue.");
            return;
        }

        Order nextOrder = processingQueue.dequeue();
        nextOrder.setStatus("Processing");
        System.out.println("  Now processing " + nextOrder.toString());
        System.out.println("  Pending orders remaining: " + getPendingCount());
    }

    public int getPendingCount() {
        return processingQueue.size();
    }

    public void printOrderHistory(User user) {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : orderHistory) {
            if (order.getUser().getUserID() == user.getUserID()) {
                userOrders.add(order);
            }
        }

        if (userOrders.isEmpty()) {
            System.out.println("  You have no order history.");
            return;
        }

        for (int i = 0; i < userOrders.size(); i++) {
            Order order = userOrders.get(i);
            System.out.println();
            System.out.println("  Order #" + order.getOrderId());
            System.out.println("  Restaurant: " + order.getRestaurant().getName());
            System.out.println("  Status: " + order.getStatus());
            System.out.println("  Total: RM " + String.format("%.2f", order.getTotalPrice()));
            System.out.println("  Items:");
            for (OrderItem item : order.getItems()) {
                System.out.printf("    - %s x %d (RM %.2f)%n", item.getFoodItem().getName(), 
                    item.getQuantity(), item.getTotalPrice());
            }
        }
    }
}