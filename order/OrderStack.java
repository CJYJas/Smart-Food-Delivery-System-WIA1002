package order;
import java.util.ArrayList;
import model.OrderItem;

public class OrderStack {
    private ArrayList<OrderItem> orderItems;

    public OrderStack(){
        orderItems = new ArrayList<OrderItem>();
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
}
