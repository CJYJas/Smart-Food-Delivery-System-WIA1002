package main.user;

import java.util.ArrayList;
import java.util.List;
import main.model.Restaurant;
import java.util.HashMap;

public class RestaurantManager {

    private List<Restaurant> restaurants;
    private HashMap<Integer, Restaurant> restaurantMap;

    public RestaurantManager() {
        this.restaurants = new ArrayList<>();
        this.restaurantMap = new HashMap<>();
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
        restaurantMap.put(restaurant.getRestaurantID(), restaurant);
        System.out.println("Restaurant added: " + restaurant.getName());
    }

    public void removeRestaurant(int restaurantID) {
        restaurants.removeIf(restaurant -> restaurant.getRestaurantID() == restaurantID);
        restaurantMap.remove(restaurantID);
        System.out.println("Restaurant removed: " + restaurantID);
    }

    public void displayRestaurant() {
        System.out.println("Restaurants:");
        for (Restaurant restaurant : restaurants) {
            System.out.println(restaurant);
        }
    }

    public Restaurant searchRestaurant(int restaurantID) {
        return restaurantMap.get(restaurantID);
    }

}