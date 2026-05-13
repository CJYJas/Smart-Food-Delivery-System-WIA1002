package order;

import java.util.List;

import model.Order;
import model.OrderItem;
import model.Restaurant;
import model.User;

public class OrderService {
    private OrderStack cartStack;
    private final OrderQueue processingQueue;

    public OrderService() {
        this.cartStack = new OrderStack(); 
        this.processingQueue = new OrderQueue();
    }

    public void createNewOrder(User user, Restaurant restaurant) {
        cartStack = new OrderStack();
        System.out.println("Ordering from " + restaurant.getName() + ". Cart cleared — add items below.");
    }

    /** Restaurant id from the first line in the cart, or -1 if the cart is empty. */
    public int getCartRestaurantId() {
        if (cartStack.isEmpty()) {
            return -1;
        }
        return cartStack.getOrderedItems().get(0).getFoodItem().getRestaurantID();
    }

    public void addItemToCart(OrderItem item) {
        cartStack.push(item);
        System.out.println("Item added: " + item.getFoodItem().getName());
    }

    public void undoLastItem() {
        if (!cartStack.isEmpty()) {
            OrderItem removed = cartStack.pop();
            System.out.println("Successfully undo " + removed.getFoodItem().getName());
        } else {
            System.out.println("Cart is empty, nothing to undo.");
        }
    }

    public void confirmOrder(int orderId, User user, Restaurant restaurant) {
        if (cartStack.isEmpty()) {
            System.out.println("Cannot place order: Cart is empty.");
            return;
        }

        Order newOrder = new Order(orderId, user, restaurant);
        while (!cartStack.isEmpty()) {
            newOrder.addItem(cartStack.pop());
        }
        processingQueue.enqueue(newOrder);
        System.out.println("Order #" + orderId + " placed successfully!");
    }

    public void printCartOverview(List<Restaurant> restaurants) {
        if (cartStack.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        int rid = getCartRestaurantId();
        for (Restaurant r : restaurants) {
            if (r.getRestaurantID() == rid) {
                System.out.println("Restaurant: " + r.getName());
                break;
            }
        }
        int n = 1;
        for (OrderItem oi : cartStack.getOrderedItems()) {
            System.out.printf("  %d. %s  x%d  RM %.2f%n",
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
        if (!processingQueue.isEmpty()) {
            Order nextOrder = processingQueue.dequeue(); 
            nextOrder.setStatus("Processing");
            System.out.println("Now processing " + nextOrder.toString());
        } else {
            System.out.println("No pending orders in the queue.");
        }
    }

    public Order getNextOrder() {
        if (!processingQueue.isEmpty()) {
            return processingQueue.peek();
        } else {
            System.out.println("No pending orders in the queue.");
            return null;
        }
    }

    public int getPendingCount() {
        return processingQueue.size();
    }
}