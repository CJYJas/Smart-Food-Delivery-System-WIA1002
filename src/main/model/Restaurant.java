package main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restaurant in the system.
 * Contains restaurant details such as ID, name, location, rating, and menu.
 */
public class Restaurant {
    private String restaurantID;
    private String name;
    private String location;
    private double rating;
    private List<FoodItem> menu;

    /**
     * Constructs a new Restaurant.
     *
     * @param restaurantID The unique identifier for the restaurant.
     * @param name         The brand name of the restaurant.
     * @param location     The hub location of the restaurant.
     * @param rating       The current rating of the restaurant.
     */
    public Restaurant(String restaurantID, String name, String location, double rating) {
        this.restaurantID = restaurantID;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.menu = new ArrayList<>();
    }

    /**
     * Retrieves the restaurant's unique ID.
     * @return the restaurant ID.
     */
    public String getRestaurantID() {
        return restaurantID;
    }

    /**
     * Sets the restaurant's unique ID.
     * @param restaurantID the new restaurant ID.
     */
    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    /**
     * Retrieves the restaurant's name.
     * @return the restaurant name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the restaurant's name.
     * @param name the new restaurant name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the restaurant's location.
     * @return the restaurant location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the restaurant's location.
     * @param location the new restaurant location.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Retrieves the restaurant's rating.
     * @return the restaurant rating.
     */
    public double getRating() {
        return rating;
    }

    /**
     * Sets the restaurant's rating.
     * @param rating the new restaurant rating.
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Returns a string representation of the restaurant.
     * @return a formatted string containing restaurant details.
     */
    @Override
    public String toString() {
        return String.format("  [%s]  %-16s  %-16s  %.1f", restaurantID, name, location, rating);
    }

    /**
     * Adds a food item to the restaurant's menu.
     * @param item the FoodItem to add.
     */
    public void addFoodItem(FoodItem item) {
        this.menu.add(item);
    }

    /**
     * Retrieves the restaurant's menu.
     * @return a list of FoodItems offered by the restaurant.
     */
    public List<FoodItem> getMenu() {
        return menu;
    }
}