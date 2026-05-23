package main;

import java.io.*;
import java.util.*;

import main.model.*;
import main.navigation.CityGraph;
import main.order.OrderService;
import main.rider.DeliveryManager;
import main.search.SearchService;
import main.user.RestaurantManager;
import main.user.UserManager;

public class App {

    public static final String USERS_FILE       = "users.txt";
    private static final String RESTAURANTS_FILE = "restaurants.csv";
    private static final String MENU_FILE        = "menu.csv";
    private static final String LOCATIONS_FILE   = "locations.csv";
    private static final String ROADS_FILE       = "roads.csv";
    private static final String RIDERS_FILE      = "riders.csv";

    public static final int LINE_WIDTH = 50;

    static final List<User>       users       = new ArrayList<>();
    static final List<Restaurant> restaurants = new ArrayList<>();
    static final List<FoodItem>   allFood     = new ArrayList<>();

    static final UserManager       userManager       = new UserManager();
    static final RestaurantManager restaurantManager = new RestaurantManager();
    static final OrderService      orderService      = new OrderService();
    static final SearchService     searchService     = new SearchService();
    static final CityGraph<String, Double> cityGraph = new CityGraph<>();

    static DeliveryManager deliveryManager;
    static int nextOrderId = 1;

    static final Scanner scanner = new Scanner(System.in);

    public static void printHeader(String title) {
        System.out.println();
        System.out.println("=".repeat(LINE_WIDTH));
        System.out.println("  " + title);
        System.out.println("=".repeat(LINE_WIDTH));
    }

    public static void printSuccess(String msg) {
        System.out.println("[SUCCESS] " + msg);
    }

    public static void printError(String msg) {
        System.out.println("[ERROR] " + msg);
    }

    public static void printDivider() {
        System.out.println("-".repeat(LINE_WIDTH));
    }

    public static void main(String[] args) {
        loadAllData();

        boolean running = true;
        while (running) {
            printHeader("SMART FOOD DELIVERY SYSTEM");
            System.out.println("  1. Admin Login");
            System.out.println("  2. User Login");
            System.out.println("  3. Sign Up");
            System.out.println("  0. Exit");
            printDivider();
            System.out.print("  Select option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> AdminMenu.adminLogin();
                case "2" -> UserMenu.userLogin();
                case "3" -> User.signup(scanner, users, userManager);
                case "0" -> {
                    running = false;
                    printHeader("GOODBYE");
                    System.out.println("  Thank you for using Smart Food Delivery System!");
                    System.out.println("=".repeat(LINE_WIDTH));
                }
                default -> printError("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void loadAllData() {
        System.out.println("Loading data...");
        User.loadUsers(users, userManager);
        loadRestaurants();
        loadMenu();
        loadLocations();
        loadRoads();
        deliveryManager = new DeliveryManager(cityGraph);
        loadRiders();
        printSuccess("All data loaded successfully.");
        System.out.println();
    }

    private static void loadRestaurants() {
        try (Scanner fs = new Scanner(new File(RESTAURANTS_FILE))) {
            while (fs.hasNextLine()) {
                String line = fs.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length >= 4) {
                    Restaurant r = new Restaurant(
                        Integer.parseInt(p[0].trim()), p[1].trim(),
                        p[2].trim(), Double.parseDouble(p[3].trim()));
                    restaurants.add(r);
                    restaurantManager.addRestaurant(r);
                }
            }
        } catch (FileNotFoundException e) {
            printError("Could not load restaurants: " + e.getMessage());
        }
    }

    private static void loadMenu() {
        try (Scanner fs = new Scanner(new File(MENU_FILE))) {
            while (fs.hasNextLine()) {
                String line = fs.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length >= 5) {
                    FoodItem item = new FoodItem(
                        Integer.parseInt(p[0].trim()), p[1].trim(),
                        Double.parseDouble(p[2].trim()), p[3].trim(),
                        Integer.parseInt(p[4].trim()));
                    allFood.add(item);
                    searchService.addFoodToMenu(item);
                    for (Restaurant r : restaurants) {
                        if (r.getRestaurantID() == item.getRestaurantID()) {
                            r.addFoodItem(item);
                            break;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            printError("Could not load menu: " + e.getMessage());
        }
    }

    private static void loadLocations() {
        try (Scanner fs = new Scanner(new File(LOCATIONS_FILE))) {
            while (fs.hasNextLine()) {
                String loc = fs.nextLine().trim();
                if (!loc.isEmpty()) cityGraph.addLocation(loc);
            }
        } catch (FileNotFoundException e) {
            printError("Could not load locations: " + e.getMessage());
        }
    }

    private static void loadRoads() {
        try (Scanner fs = new Scanner(new File(ROADS_FILE))) {
            while (fs.hasNextLine()) {
                String line = fs.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length >= 3)
                    cityGraph.addRoad(p[0].trim(), p[1].trim(), Double.parseDouble(p[2].trim()));
            }
        } catch (FileNotFoundException e) {
            printError("Could not load roads: " + e.getMessage());
        }
    }

    private static void loadRiders() {
        try (Scanner fs = new Scanner(new File(RIDERS_FILE))) {
            while (fs.hasNextLine()) {
                String line = fs.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length >= 2)
                    deliveryManager.addRider(p[0].trim(), Double.parseDouble(p[1].trim()));
            }
        } catch (FileNotFoundException e) {
            printError("Could not load riders: " + e.getMessage());
        }
    }
}
