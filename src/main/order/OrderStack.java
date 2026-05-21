package main.order;

import java.util.ArrayList;
import main.model.OrderItem;

public class OrderStack {
    private final ArrayList<OrderItem> orderItems;

    public OrderStack(){
        orderItems = new ArrayList<>();
    }

    public void push(OrderItem item){
        orderItems.add(item);
    }
    
    public OrderItem pop(){
        OrderItem o = orderItems.get(getSize() - 1);
        orderItems.remove(getSize() - 1);
        return o;
    }
    
    public OrderItem peek(){
        return orderItems.get(getSize() - 1);
    }
    
    public int getSize(){
        return orderItems.size();
    }
    
    public boolean isEmpty(){
        return orderItems.isEmpty();
    }

    /** Items in add order (first added first); stack top is last element. */
    public List<OrderItem> getOrderedItems() {
        return Collections.unmodifiableList(new ArrayList<>(orderItems));
    }
}