package main.model;

public class FoodItem {

    private int itemID;
    private String name;
    private double price;
    private String category;
    private String restaurantID;

    public FoodItem(int itemID, String name, double price, String category, String restaurantID) {
        this.itemID = itemID;
        this.name = name;
        this.price = price;
        this.category = category;
        this.restaurantID = restaurantID;
    }

    public int getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    @Override
    public String toString() {
        return String.format("[%d] %-16s | RM%6.2f | (%s)", itemID, name, price, category);
    }
}