import model.*;
import navigation.*;
import order.*;
import search.*;
import rider.*;
import java.util.*;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderService orderService = new OrderService();
    private static final CityGraph<String, Integer> map = new CityGraph<>();
    //private static final BST foodSearch = new BST();
    
    private static User currentUser;
    private static final List<Restaurant> restaurants = new ArrayList<>();

    public static void main(String[] args) {
        initializeData();
        
        System.out.println("===========================================");
        System.out.println("   WELCOME TO SMART FOOD DELIVERY SYSTEM   ");
        System.out.println("===========================================");
        
        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Search Food/Restaurants");
            System.out.println("3. Place Order");
            System.out.println("4. Manage Current Order (Undo/Confirm)");
            System.out.println("5. View Shortest Delivery Path");
            System.out.println("6. Process Next Order (Admin)");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1 -> login();
                case 2 -> searchMenu();
                case 3 -> startOrdering();
                case 4 -> manageOrder();
                case 5 -> viewPath();
                case 6 -> orderService.processNextOrder();
                case 0 -> {
                    System.out.println("Thank you for using Smart Food Delivery System!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void initializeData() {
        // Initialize User
        currentUser = new User(1, "Jasmine", "pass123", "jasmine@example.com", "0123456789", "User Home");

        // Initialize Map
        map.addLocation("User Home");
        map.addLocation("McD");
        map.addLocation("KFC");
        map.addLocation("Pizza Hut");
        map.addLocation("Central Mall");
        
        map.addRoad("User Home", "McD", 5.0);
        map.addRoad("McD", "User Home", 5.0);
        map.addRoad("McD", "Central Mall", 3.0);
        map.addRoad("Central Mall", "McD", 3.0);
        map.addRoad("User Home", "KFC", 8.0);
        map.addRoad("KFC", "User Home", 8.0);
        map.addRoad("KFC", "Pizza Hut", 2.0);
        map.addRoad("Pizza Hut", "KFC", 2.0);
        map.addRoad("Pizza Hut", "User Home", 6.0);
        map.addRoad("User Home", "Pizza Hut", 6.0);

        // Initialize Restaurants & Food
        Restaurant mcd = new Restaurant(1, "McD", "McD Street", 4.5);
        FoodItem burger = new FoodItem(101, "Big Mac", 15.50, "Burger", 1);
        FoodItem fries = new FoodItem(102, "Fries", 6.00, "Side", 1);
        mcd.addFoodItem(burger);
        mcd.addFoodItem(fries);
        
        restaurants.add(mcd);
        foodSearch.insert(burger);
        foodSearch.insert(fries);

        System.out.println("System data initialized successfully.");
    }

    private static void login() {
        System.out.println("\n--- LOGIN ---");
        System.out.println("Logged in as: " + currentUser.getUsername());
    }

    // private static void searchMenu() {
    //     System.out.println("\n--- SEARCH ---");
    //     System.out.print("Enter food name to search: ");
    //     String query = scanner.nextLine();
    //     FoodItem found = foodSearch.search(query);
    //     if (found != null) {
    //         System.out.println("Found: " + found);
    //     } else {
    //         System.out.println("Item not found.");
    //     }
    // }

    private static void startOrdering() {
        if (currentUser == null) {
            System.out.println("Please login first.");
            return;
        }
        System.out.println("\n--- PLACE ORDER ---");
        System.out.println("Select a restaurant:");
        for (int i = 0; i < restaurants.size(); i++) {
            System.out.println((i + 1) + ". " + restaurants.get(i).getName());
        }
        int resChoice = scanner.nextInt() - 1;
        scanner.nextLine();
        
        if (resChoice >= 0 && resChoice < restaurants.size()) {
            Restaurant selected = restaurants.get(resChoice);
            orderService.createNewOrder(currentUser, selected);
            System.out.println("Ordering from " + selected.getName());
            
            List<FoodItem> menu = selected.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                System.out.println((i + 1) + ". " + menu.get(i));
            }
            
            System.out.print("Enter item number to add (0 to stop): ");
            int itemChoice;
            while ((itemChoice = scanner.nextInt()) != 0) {
                if (itemChoice > 0 && itemChoice <= menu.size()) {
                    OrderItem oi = new OrderItem(menu.get(itemChoice - 1), 1);
                    orderService.addItemToCart(oi);
                }
                System.out.print("Add another? (0 to stop): ");
            }
            scanner.nextLine();
        }
    }

    private static void manageOrder() {
        System.out.println("\n--- MANAGE ORDER ---");
        System.out.println("1. Undo last item");
        System.out.println("2. Confirm Order");
        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 1) {
            orderService.undoLastItem();
        } else if (choice == 2) {
            orderService.confirmOrder(new Random().nextInt(1000), currentUser, restaurants.get(0));
        }
    }

    private static void viewPath() {
        System.out.println("\n--- DELIVERY PATH ---");
        System.out.print("Enter Restaurant Name: ");
        String resName = scanner.nextLine();
        if (map.hasLocation(resName)) {
            double distance = map.getShortestDistance(resName, "User Home");
            List<String> path = map.getShortestPath(resName, "User Home");
            System.out.println("Shortest distance to Home: " + distance + " km");
            System.out.println("Path: " + String.join(" -> ", path));
        } else {
            System.out.println("Restaurant location not found on map.");
        }
    }
}
