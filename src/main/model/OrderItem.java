package main.model;

/**
 * Represents an individual item in a customer's order, including the food item and quantity.
 */
public class OrderItem {
    // The food item being ordered
    private FoodItem foodItem;

    // Quantity of the food item ordered
    private int quantity;

    /**
    * Constructs a new OrderItem object.
    *
    * @param foodItem The food item being ordered
    * @param quantity The quantity of the food item ordered
    */
    public OrderItem(FoodItem foodItem, int quantity) {
        this.foodItem = foodItem;
        this.quantity = quantity;
    }

    // Getters
    public FoodItem getFoodItem() {
        return foodItem;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public double getTotalPrice() {
        return foodItem.getPrice() * quantity;
    }
}