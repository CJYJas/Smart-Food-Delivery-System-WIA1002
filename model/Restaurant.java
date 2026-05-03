package model;

import java.util.List;
import java.util.ArrayList;

public class Restaurant {
    private int restaurantID;
    private String name;
    private String location;
    private double rating;
    private List<FoodItem> menu;

    public Restaurant(int restaurantID, String name, String location, double rating) {
        this.restaurantID = restaurantID;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.menu = new ArrayList<>();
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String toString() {
        return String.format("%-5s %-25s %-15s %.1f/5.0 | Items: %d", "[ID: " + restaurantID + "]", name, location,
                rating, menu.size());
    }

    public void addFoodItem(FoodItem item) {
        this.menu.add(item);
    }
}