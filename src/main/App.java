package main;

import java.io.*;
import java.util.*;
import main.model.*;
import main.navigation.*;
import main.order.*;
import main.search.*;
import main.user.*;
import main.rider.*;

public class App {
    public static final String USERS_FILE = "data/users.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderService orderService = new OrderService();
    private static final SearchService searchService = new SearchService();
    private static final CityGraph<String, Double> map = new CityGraph<>();
    private static final UserManager userManager = new UserManager();
    private static final RestaurantManager restaurantManager = new RestaurantManager();
    private static final DeliveryManager deliveryManager = new DeliveryManager(map);

    private static User currentUser;

    private static final List<Restaurant> restaurants = new ArrayList<>();

    public static void main(String[] args) {
        initializeData();
        User.loadUsers(userManager);

        printHeader("SMART FOOD DELIVERY SYSTEM");

        while (true) {
            if (currentUser == null) {
                guestMenu();
            } else {
                mainMenu();
            }
        }
    }

    private static void guestMenu() {
        printHeader("HOME PORTAL");

        System.out.println("   ┌────────────────────────────────────────┐");
        System.out.println("   │  [1] Login                             │");
        System.out.println("   │  [2] Sign Up                           │");
        System.out.println("   │  [0] Exit System                       │");
        System.out.println("   └────────────────────────────────────────┘");

        System.out.print("\n ➜ Select option: ");
        String input = scanner.nextLine().trim();

        switch (input) {
            case "1" -> {
                User loggedIn = User.login(scanner, userManager);
                if (loggedIn != null)
                    currentUser = loggedIn;
            }
            case "2" -> User.signup(scanner, userManager);
            case "0" -> exitApp();
            default -> printError("Invalid option. Please try again.");
        }
    }

    private static void mainMenu() {
        printHeader("DASHBOARD • " + currentUser.getUsername().toUpperCase());

        System.out.println("   ┌────────────────────────────────────────┐");
        System.out.println("   │ [1] View Restaurants & Menus           │");
        System.out.println("   │ [2] Search Food                        │");
        System.out.println("   │ [3] Search Restaurant                  │");
        System.out.println("   │ [4] Place New Order                    │");
        System.out.println("   │ [5] Manage Active Order                │");
        System.out.println("   │ [6] Shortest Delivery Path             │");
        System.out.println("   │ [7] Process Next Order                 │");
        System.out.println("   │ [8] Logout                             │");
        System.out.println("   │ [0] Exit Application                   │");
        System.out.println("   └────────────────────────────────────────┘");

        System.out.print("\n ➜ Choose option: ");
        String input = scanner.nextLine().trim();

        switch (input) {
            case "1" -> viewRestaurants();
            case "2" -> searchFood();
            case "3" -> searchRestaurant();
            case "4" -> startOrdering();
            case "5" -> manageOrder();
            case "6" -> viewPath();
            case "7" -> {
                printStatus("Processing next order...");
                orderService.processNextOrder();
                deliveryManager.assignBestRider();
            }
            case "8" -> {
                currentUser = null;
                printSuccess("You have logged out.");
            }
            case "0" -> exitApp();
            default -> printError("Invalid option.");
        }
    }

    private static void exitApp() {
        System.out.println("\n 🌟 Thank you for using Smart Food Delivery System!\n");
        System.exit(0);
    }

    private static void initializeData() {
        try (Scanner sc = new Scanner(new File("data/locations.csv"))) {
            while (sc.hasNextLine()) {
                String loc = sc.nextLine().trim();
                if (!loc.isEmpty())
                    map.addLocation(loc);
            }
        } catch (FileNotFoundException ignored) {
        }

        try (Scanner sc = new Scanner(new File("data/roads.csv"))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts.length == 3) {
                    map.addRoad(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim()));
                }
            }
        } catch (FileNotFoundException ignored) {
        }

        try (Scanner sc = new Scanner(new File("data/restaurants.csv"))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts.length == 4) {
                    Restaurant r = new Restaurant(Integer.parseInt(parts[0].trim()), parts[1].trim(), parts[2].trim(),
                            Double.parseDouble(parts[3].trim()));
                    restaurants.add(r);
                    restaurantManager.addRestaurant(r);
                }
            }
        } catch (FileNotFoundException ignored) {
        }

        try (Scanner sc = new Scanner(new File("data/menu.csv"))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts.length == 5) {
                    FoodItem item = new FoodItem(Integer.parseInt(parts[0].trim()), parts[1].trim(),
                            Double.parseDouble(parts[2].trim()), parts[3].trim(), Integer.parseInt(parts[4].trim()));
                    Restaurant r = restaurantManager.searchRestaurant(item.getRestaurantID());
                    if (r != null) {
                        addFood(r, item);
                    }
                }
            }
        } catch (FileNotFoundException ignored) {
        }

        try (Scanner sc = new Scanner(new File("data/riders.csv"))) {
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts.length == 2) {
                    deliveryManager.addRider(parts[0].trim(), Double.parseDouble(parts[1].trim()));
                }
            }
        } catch (FileNotFoundException ignored) {
        }

        System.out.println("✔ System ready: Loaded " + restaurants.size() + " restaurants.");
    }

    private static void addFood(Restaurant r, FoodItem item) {
        r.addFoodItem(item);
        searchService.addFoodToMenu(item);
    }

    private static void viewRestaurants() {
        printHeader("RESTAURANTS DIRECTORY");
        restaurantManager.displayRestaurant();
        printDivider();
        Integer choice = readOptionalInt(" ➜ Choose restaurant ID (0 to go back): ", 0, 100);
        if (choice == null || choice == 0)
            return;

        Restaurant selected = restaurantManager.searchRestaurant(choice);
        if (selected == null) {
            printError("Invalid restaurant ID.");
            return;
        }
        printHeader("CATALOGUE — " + selected.getName().toUpperCase());
        for (FoodItem item : selected.getMenu()) {
            System.out.println("  • " + item);
        }
        printDivider();
    }

    private static void searchFood() {
        printHeader("SEARCH FOOD");
        System.out.print(" 🔍 Enter item keywords (e.g. Big Mac): ");
        String query = scanner.nextLine().trim();
        if (query.isEmpty()) {
            printError("Search token cannot be empty.");
            return;
        }
        FoodItem found = searchService.findFood(query);
        if (found != null) {
            System.out.println("\n  🎉 Match Found: " + found);
        } else {
            printError("No exact catalog options matched that keyword.");
        }
    }

    private static void searchRestaurant() {
        printHeader("SEARCH RESTAURANT");
        System.out.print(" 🔍 Enter partial or full match query: ");
        String query = scanner.nextLine().trim().toLowerCase();
        if (query.isEmpty()) {
            printError("Search token cannot be empty.");
            return;
        }
        boolean found = false;
        System.out.println();
        for (Restaurant r : restaurants) {
            if (r.getName().toLowerCase().contains(query)) {
                System.out.println("  📍 " + r.getName() + " (" + r.getLocation() + ") — ★ " + r.getRating());
                found = true;
            }
        }
        if (!found) {
            printError("No restaurant found.");
        }
    }

    private static void startOrdering() {
        if (currentUser == null) {
            printError("Session expired or missing. Please authenticate.");
            return;
        }
        printHeader("CREATE NEW ORDER BASKET");
        for (int i = 0; i < restaurants.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + restaurants.get(i).getName());
        }
        printDivider();
        Integer resChoice = readOptionalInt(" ➜ Target Restaurant ID (0 to Cancel): ", 0, restaurants.size());
        if (resChoice == null || resChoice == 0)
            return;

        Restaurant selected = restaurantManager.searchRestaurant(resChoice);
        if (selected == null) {
            printError("Invalid restaurant ID.");
            return;
        }
        orderService.createNewOrder(currentUser, selected);

        List<FoodItem> menu = selected.getMenu();
        printHeader("BASKET: ADDING FROM " + selected.getName().toUpperCase());
        for (int i = 0; i < menu.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + menu.get(i));
        }
        printDivider();
        System.out.println("Press Enter without typing anything to finish.");

        while (true) {
            System.out.print("Select item number (Enter to finish): ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                break;
            try {
                int itemId = Integer.parseInt(line);
                boolean found = false;

                for (FoodItem item : menu) {
                    if (item.getItemID() == itemId) {
                        OrderItem oi = new OrderItem(item, 1);
                        orderService.addItemToCart(oi);

                        System.out.println("✔ Added: " + item.getName());
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println("❌ Invalid item ID.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        printDivider();
        orderService.printCartOverview(restaurants);
        System.out.printf(" Pending Cart Value: RM %.2f%n", orderService.getCartTotal());
        System.out.println("Go to Manage Order to confirm your order.");
    }

    private static void manageOrder() {
        printHeader("MANAGE ACTIVE BASKETS");
        orderService.printCartOverview(restaurants);
        if (!orderService.isCartEmpty()) {
            System.out.printf(" Consolidated Total: RM %.2f%n", orderService.getCartTotal());
        }
        printDivider();
        System.out.println("  [1] Undo Last Item");
        System.out.println("  [2] Confirm Order");
        System.out.println("  [0] Back");
        printDivider();
        Integer choice = readOptionalInt(" ➜ Selected action option: ", 0, 2);
        if (choice == null || choice == 0)
            return;

        if (choice == 1) {
            orderService.undoLastItem();
            printSuccess("Last item removed.");
            return;
        }
        int rid = orderService.getCartRestaurantId();
        if (rid < 0) {
            printError("Your cart is empty.");
            return;
        }
        Restaurant r = restaurantManager.searchRestaurant(rid);
        if (r == null) {
            printError("Restaurant not found.");
            return;
        }
        int orderId = (int) (System.currentTimeMillis() % 900000) + 100000;
        orderService.confirmOrder(orderId, currentUser, r);
        printSuccess("Order confirmed! Order ID: #" + orderId);
    }

    private static void viewPath() {
        printHeader("SHORTEST DELIVERY PATH");
        for (int i = 0; i < restaurants.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + restaurants.get(i).getName());
        }
        printDivider();
        Integer choice = readOptionalInt(" ➜ Origin Restaurant ID (0 to Abort): ", 0, restaurants.size());
        if (choice == null || choice == 0)
            return;

        String resName = restaurants.get(choice - 1).getName();
        if (!map.hasLocation(resName)) {
            printError("Restaurant location not found.");
            return;
        }
        double distance = map.getShortestDistance(resName, "User Home");
        List<String> path = map.getShortestPath(resName, "User Home");
        if (distance >= Double.MAX_VALUE / 2 || path.isEmpty()) {
            printError("No delivery route found.");
        } else {
            System.out.println("\nShortest Delivery Route");
            System.out.printf("Distance: %.2f km%n", distance);
            System.out.println("Route: " + String.join(" -> ", path));
        }
    }

    // --- High-Fidelity UI Helpers ---

    public static void printHeader(String title) {
        System.out.println();
        int width = 50;
        String lineBorder = "═".repeat(width);

        int padTotal = width - title.length() - 2;
        int leftPad = padTotal / 2;
        int rightPad = padTotal - leftPad;

        System.out.println(lineBorder);
        System.out.println("║" + " ".repeat(leftPad) + title + " ".repeat(rightPad) + "║");
        System.out.println(lineBorder);
        System.out.println();
    }

    public static void printDivider() {
        System.out.println("  ----------------------------------------");
    }

    public static void printError(String msg) {
        System.out.println("\n  ❌ Error: " + msg + "\n");
    }

    public static void printSuccess(String msg) {
        System.out.println("\n  ✔ Success: " + msg + "\n");
    }

    public static void printStatus(String msg) {
        System.out.println("  ⏳ " + msg);
    }

    private static Integer readOptionalInt(String prompt, int min, int max) {
        for (int attempt = 0; attempt < 5; attempt++) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v >= min && v <= max) {
                    return v;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
        printError("Too many invalid attempts.");
        return null;
    }
}