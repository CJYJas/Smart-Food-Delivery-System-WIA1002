package main.user;

import main.dataStructure.MyLinkedList;
import main.model.Restaurant;

/**
 * Manages a collection of restaurants in the system.
 * Provides functionality to add, remove, search, and display restaurants.
 * Utilizes a custom linked list implementation for storage.
 */
public class RestaurantManager {

    private MyLinkedList<Restaurant> restaurants;

    /**
     * Constructs a new RestaurantManager with an empty list of restaurants.
     */
    public RestaurantManager() {
        this.restaurants = new MyLinkedList<>();
    }

    /**
     * Adds a new restaurant to the manager.
     * Prevents adding a restaurant if its ID already exists.
     *
     * @param restaurant The restaurant to add.
     */
    public void addRestaurant(Restaurant restaurant) {
        if (searchRestaurant(restaurant.getRestaurantID()) != null) {
            System.out.println("    Restaurant with ID " + restaurant.getRestaurantID() + " already exists.");
            return;
        }
        restaurants.add(restaurant);
        System.out.println("    Restaurant added: " + restaurant.getName());
    }

    /**
     * Removes a restaurant from the manager based on its ID.
     *
     * @param restaurantID The ID of the restaurant to remove.
     * @return true if the restaurant was successfully removed, false otherwise.
     */
    public boolean removeRestaurant(String restaurantID) {
        for (int i = 0; i < restaurants.getSize(); i++) {
            Restaurant restaurant = restaurants.get(i);
            if (restaurant.getRestaurantID().equals(restaurantID)) {
                restaurants.remove(i);
                System.out.println("    Restaurant removed: " + restaurant.getName());
                return true;
            }
        }
        System.out.println("    Restaurant with ID " + restaurantID + " not found.");
        return false;
    }

    /**
     * Displays a formatted table of all restaurants currently managed.
     */
    public void displayRestaurant() {
        System.out.printf("  %-5s   %-16s  %-16s  %s%n", "ID", "Brand Name", "Hub Location", "Rating");
        System.out.println("-".repeat(50));
        for (Restaurant restaurant : restaurants) {
            System.out.println(restaurant.toString());
        }
    }

    /**
     * Searches for a restaurant by its ID.
     *
     * @param restaurantID The ID of the restaurant to search for.
     * @return the Restaurant object if found, null otherwise.
     */
    public Restaurant searchRestaurant(String restaurantID) {
        for (int i = 0; i < restaurants.getSize(); i++) {
            Restaurant restaurant = restaurants.get(i);
            if (restaurant.getRestaurantID().equals(restaurantID)) {
                return restaurant;
            }
        }
        return null;
    }

}