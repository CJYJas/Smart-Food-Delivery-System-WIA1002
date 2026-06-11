package main.order;

import java.util.ArrayList;
import java.util.List;
import main.model.Order;
import main.model.OrderItem;
import main.model.Restaurant;
import main.model.User;

/**
 * Service that manages cart, order confirmation, and processing.
 * Uses an OrderStack for the cart and an OrderQueue for pending orders.
 */
public class OrderService {

    // Stack to manage the current cart items (LIFO)
    private OrderStack cartStack;

    // Queue to manage confirmed orders (FIFO)
    private final OrderQueue processingQueue;

    // Map to store order history by order ID for quick retrieval
    private final java.util.HashMap<Integer, Order> orderHistory;

    /**
     * Creates a new order service with empty cart, processing queue, and order history.
     */
    public OrderService() {
        this.cartStack = new OrderStack();
        this.processingQueue = new OrderQueue();
        this.orderHistory = new java.util.HashMap<>();
    }

    /**
     * Creates a new order with the specified user and restaurant.
     *
     * @param user The user placing the order
     * @param restaurant The restaurant the order is for
     */
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

    /**
     * Adds an item to the cart stack.
     *
     * @param item The OrderItem to be added to the cart
     */
    public void addItemToCart(OrderItem item) {
        cartStack.push(item);
        System.out.println("  Item added: " + item.getFoodItem().getName());
    }

    /**
     * Undoes the last item added to the cart stack.
     * If the cart is empty, it will print a message indicating that there is nothing to undo.
     */
    public void undoLastItem() {
        if (!cartStack.isEmpty()) {
            OrderItem removed = cartStack.pop();
            System.out.println("  Successfully undo " + removed.getFoodItem().getName() + ".");
        } else {
            System.out.println("  Cart is empty, nothing to undo.");
        }
    }

    /**
     * Confirms the current cart as an order and adds it to the processing queue.
     *
     * @param orderId The ID of the new order
     * @param user The user placing the order
     * @param restaurant The restaurant the order is for
     */
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
        orderHistory.put(orderId, newOrder);
        System.out.println("  Order #" + orderId + " placed successfully!");
    }

    /**
    * Prints an overview of the current cart, including the restaurant and items.
    *
    * @param restaurants The list of all restaurants to find the restaurant name
    */
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
            System.out.printf("  %d. %18s   x %d    RM %.2f%n",
                n++, oi.getFoodItem().getName(), oi.getQuantity(), oi.getTotalPrice());
        }
    }

    /**
     * Calculates the total price of the items currently in the cart.
     *
     * @return The total price of the cart
     */
    public double getCartTotal() {
        double t = 0;
        for (OrderItem oi : cartStack.getOrderedItems()) {
            t += oi.getTotalPrice();
        }
        return t;
    }

    /**
     * Checks if the cart is empty.
     *
     * @return true if the cart is empty, false otherwise
     */
    public boolean isCartEmpty() {
        return cartStack.isEmpty();
    }

    /**
     * Processes the next order in the processing queue.
     * If there are no pending orders, it will print a message indicating that the queue is empty.
     */
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

    /**
     * Returns the number of pending orders in the processing queue.
     *
     * @return The number of pending orders
     */
    public int getPendingCount() {
        return processingQueue.size();
    }

    /**
     * Retrieves the details of a specific order by its ID.
     *
     * @param orderId The ID of the order to retrieve
     * @return The Order object with the specified ID, or null if not found
     */
    public Order getOrderDetails(int orderId) {
        return orderHistory.get(orderId);
    }

    /**
     * Prints the order history for a specific user.
     *
     * @param user The user for whom to print order history
     */
    public void printOrderHistory(User user) {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : orderHistory.values()) {
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