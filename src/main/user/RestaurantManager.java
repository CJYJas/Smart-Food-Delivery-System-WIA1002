package main.user;

import java.util.LinkedList;
import main.model.Restaurant;

public class RestaurantManager {

    private LinkedList<Restaurant> restaurants;

    public RestaurantManager() {
        this.restaurants = new LinkedList<>();
    }

    public void addRestaurant(Restaurant restaurant) {
        if(searchRestaurant(restaurant.getRestaurantID()) != null) {
            System.out.println("Restaurant with ID " + restaurant.getRestaurantID() + " already exists.");
            return;
        }
        restaurants.add(restaurant);
        System.out.println("Restaurant added: " + restaurant.getName());
    }

    public boolean removeRestaurant(String restaurantID) {
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant restaurant = restaurants.get(i);
            if (restaurant.getRestaurantID().equals(restaurantID)) {
                restaurants.remove(i);
                System.out.println("Restaurant removed: " + restaurant.getName());
                return true;
            }
        }
        System.out.println("Restaurant with ID " + restaurantID + " not found.");
        return false;
    }

    public void displayRestaurant() {
        System.out.printf("  %-5s   %-16s  %-16s  %s%n", "ID", "Brand Name", "Hub Location", "Rating");
        System.out.println("-".repeat(50));
        for (Restaurant restaurant : restaurants) {
            System.out.println(restaurant.toString());
        }
    }

    public Restaurant searchRestaurant(String restaurantID) {
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant restaurant = restaurants.get(i);
            if (restaurant.getRestaurantID().equals(restaurantID)) {
                return restaurant;
            }
        }
        return null;
    }

}