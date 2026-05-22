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

    public boolean removeRestaurant(int restaurantID) {
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant restaurant = restaurants.get(i);
            if (restaurant.getRestaurantID() == restaurantID) {
                restaurants.remove(i);
                System.out.println("Restaurant removed: " + restaurant.getName());
                return true;
            }
        }        System.out.println("Restaurant with ID " + restaurantID + " not found.");
        return false;
    }

    public void displayRestaurant() {
        System.out.println("Restaurants:");
        for (Restaurant restaurant : restaurants) {
            System.out.println(restaurant);
        }
    }

    public Restaurant searchRestaurant(int restaurantID) {
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant restaurant = restaurants.get(i);
            if (restaurant.getRestaurantID() == restaurantID) {
                return restaurant;
            }
        }
        return null;
    }

}