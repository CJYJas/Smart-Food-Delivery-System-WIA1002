package order;

import model.Order;
import model.OrderItem;
import model.User;
import model.Restaurant;

public class OrderService {
    private OrderStack cartStack;
    private OrderQueue processingQueue;

    public OrderService() {
        this.cartStack = new OrderStack(); 
        this.processingQueue = new OrderQueue();
    }

    public void createNewOrder(User user, Restaurant restaurant) {
        cartStack = new OrderStack(); 
        System.out.println("New order created. Cart is now empty.");
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
