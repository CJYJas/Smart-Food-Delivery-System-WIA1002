package main.model;

/**
 * Represents a food item offered by a restaurant, including its name, price, category and associated restaurant.
 */
public class FoodItem {

    /** Unique identifier for the food item. */
    private int itemID;

    /** Display name of the food item. */
    private String name;

    /** Price of the food item in Malaysian Ringgit (RM). */
    private double price;

    /** Category or type of the food item (e.g., "Main", "Dessert", "Beverage"). */
    private String category;

    /** ID of the restaurant that offers this food item. This links the item to its restaurant. */
    private String restaurantID;

    /** Constructs a new FoodItem with the specified attributes. 
     * 
     * @param itemID unique identifier for the food item
     * @param name display name of the food item
     * @param price price of the food item in RM
     * @param category category or type of the food item
     * @param restaurantID ID of the restaurant that offers this food item
    */
    public FoodItem(int itemID, String name, double price, String category, String restaurantID) {
        this.itemID = itemID;
        this.name = name;
        this.price = price;
        this.category = category;
        this.restaurantID = restaurantID;
    }

    /** Returns the unique item ID. */
    public int getItemID() {
        return itemID;
    }

    /** Returns the display name of the item. */
    public String getName() {
        return name;
    }

    /** Returns the price of the item in RM. */
    public double getPrice() {
        return price;
    }

    /** Returns the category of the item (e.g., "Main", "Dessert"). */
    public String getCategory() {
        return category;
    }

    /** Returns the restaurant ID associated with this item. */
    public String getRestaurantID() {
        return restaurantID;
    }

    /** Sets the unique item ID. */
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    /** Sets the display name of the item. */
    public void setName(String name) {
        this.name = name;
    }

    /** Sets the price of the item in RM. */
    public void setPrice(double price) {
        this.price = price;
    }

    /** Sets the category of the item (e.g., "Main", "Dessert"). */
    public void setCategory(String category) {
        this.category = category;
    }

    /** Sets the restaurant ID associated with this item. */
    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    /**
     * Returns a string representation of the food item, including its ID, name, price and category.
     * The format is: [itemID] name | RM price | (category)
     * For example: [101] Chicken Rice | RM 5.50 | (Main)
     */
    @Override
    public String toString() {
        return String.format("[%d] %-16s | RM%6.2f | (%s)", itemID, name, price, category);
    }
}