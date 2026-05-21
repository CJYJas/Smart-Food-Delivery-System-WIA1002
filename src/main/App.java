package main;

import java.io.*;
import java.util.*;
import main.model.*;
import main.navigation.*;
import main.order.*;
import main.search.*;

public class App {
    private static final String USERS_FILE = "users.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderService orderService = new OrderService();
    private static final SearchService searchService = new SearchService();
    private static final CityGraph<String, Integer> map = new CityGraph<>();

    private static User currentUser;
    private static final List<User> users = new ArrayList<>();
    private static final List<Restaurant> restaurants = new ArrayList<>();

    public static void main(String[] args) {
        initializeData();
        loadUsers();

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
            case "1" -> login();
            case "2" -> signup();
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
        map.addLocation("User Home");
        map.addLocation("McD");
        map.addLocation("KFC");
        map.addLocation("Pizza Hut");
        map.addLocation("Subway");
        map.addLocation("Sushi King");
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
        map.addRoad("Subway", "Central Mall", 2.5);
        map.addRoad("Central Mall", "Subway", 2.5);
        map.addRoad("Sushi King", "Central Mall", 4.0);
        map.addRoad("Central Mall", "Sushi King", 4.0);
        map.addRoad("Subway", "User Home", 7.5);
        map.addRoad("User Home", "Subway", 7.5);

        Restaurant mcd = new Restaurant(1, "McD", "McD Street", 4.5);
        addFood(mcd, new FoodItem(101, "Big Mac", 15.50, "Burger", 1));
        addFood(mcd, new FoodItem(102, "Fries", 6.00, "Side", 1));
        addFood(mcd, new FoodItem(103, "McChicken", 12.00, "Burger", 1));
        restaurants.add(mcd);

        Restaurant kfc = new Restaurant(2, "KFC", "KFC Avenue", 4.3);
        addFood(kfc, new FoodItem(201, "Zinger Burger", 14.50, "Burger", 2));
        addFood(kfc, new FoodItem(202, "2-pc Combo", 18.00, "Chicken", 2));
        addFood(kfc, new FoodItem(203, "Whipped Potato", 5.50, "Side", 2));
        restaurants.add(kfc);

        Restaurant pizzaHut = new Restaurant(3, "Pizza Hut", "Pizza Plaza", 4.2);
        addFood(pizzaHut, new FoodItem(301, "Pepperoni Pizza", 25.00, "Pizza", 3));
        addFood(pizzaHut, new FoodItem(302, "Garlic Bread", 8.00, "Side", 3));
        addFood(pizzaHut, new FoodItem(303, "Spaghetti Carbonara", 16.50, "Pasta", 3));
        restaurants.add(pizzaHut);

        Restaurant subway = new Restaurant(4, "Subway", "Central Mall", 4.6);
        addFood(subway, new FoodItem(401, "Italian BMT", 14.90, "Sandwich", 4));
        addFood(subway, new FoodItem(402, "Roasted Chicken", 13.50, "Sandwich", 4));
        addFood(subway, new FoodItem(403, "Chocolate Chip Cookie", 2.50, "Dessert", 4));
        restaurants.add(subway);

        Restaurant sushiKing = new Restaurant(5, "Sushi King", "Central Mall", 4.4);
        addFood(sushiKing, new FoodItem(501, "Salmon Sushi", 6.00, "Sushi", 5));
        addFood(sushiKing, new FoodItem(502, "Ebi Tempura", 12.00, "Side", 5));
        addFood(sushiKing, new FoodItem(503, "Chicken Teriyaki Don", 15.00, "Main", 5));
        restaurants.add(sushiKing);

        System.out.println("✔ System ready: Loaded " + restaurants.size() + " restaurants.");
    }

    private static void addFood(Restaurant r, FoodItem item) {
        r.addFoodItem(item);
        searchService.addFoodToMenu(item);
    }

    private static void login() {
        printHeader("USER LOGIN");
        System.out.print(" 👤 Username: ");
        String username = scanner.nextLine().trim();
        System.out.print(" 🔑 Password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                currentUser = user;
                printSuccess("Login successful! Welcome back, " + currentUser.getUsername() + ".");
                return;
            }
        }
        printError("Access denied. Invalid credentials.");
    }

    private static void signup() {
        printHeader("SIGN UP");
        System.out.print(" 👤 Username: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            printError("Username cannot be empty.");
            return;
        }
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                printError("Username already exists.");
                return;
            }
        }
        System.out.print(" 🔑 Password: ");
        String password = scanner.nextLine();
        System.out.print(" ✉️ Email: ");
        String email = scanner.nextLine().trim();
        System.out.print(" 📞 Contact Number: ");
        String phone = scanner.nextLine().trim();
        System.out.print(" 🏠 Address: ");
        String address = scanner.nextLine().trim();

        int newId = users.isEmpty() ? 1 : users.stream().mapToInt(User::getUserID).max().orElse(0) + 1;
        User newUser = new User(newId, username, password, email, phone, address);
        users.add(newUser);
        saveUser(newUser);
        printSuccess("Account created successfully.");
    }

    private static void loadUsers() {
        try (Scanner fileScanner = new Scanner(new File(USERS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    users.add(new User(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4],
                        parts[5]
                    ));
                }
            }
        } catch (FileNotFoundException e) {
            // Safe fallback
        }
    }

    private static void saveUser(User user) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(USERS_FILE, true)))) {
            out.println(String.format("%d|%s|%s|%s|%s|%s",
                user.getUserID(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress()
            ));
        } catch (IOException e) {
            printError("Storage Write Fault: " + e.getMessage());
        }
    }

    private static void viewRestaurants() {
        printHeader("RESTAURANTS DIRECTORY");
        System.out.printf("  %-4s  %-16s  %-16s %-10s%n", "ID", "Brand Name", "Hub Location", "Rating");
        printDivider();
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant r = restaurants.get(i);
            System.out.printf("  [%d]  %-16s  %-16s ★ %.1f%n",
                i + 1, r.getName(), r.getLocation(), r.getRating());
        }
        printDivider();
        Integer choice = readOptionalInt(" ➜ Choose restaurant (0 to go back): ", 0, restaurants.size());
        if (choice == null || choice == 0) return;

        Restaurant selected = restaurants.get(choice - 1);
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
        if (resChoice == null || resChoice == 0) return;

        Restaurant selected = restaurants.get(resChoice - 1);
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
            if (line.isEmpty()) break;
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
        if (choice == null || choice == 0) return;

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
        Restaurant r = null;
        for (Restaurant x : restaurants) {
            if (x.getRestaurantID() == rid) {
                r = x;
                break;
            }
        }
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
        if (choice == null || choice == 0) return;

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

    private static void printHeader(String title) {
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

    private static void printDivider() {
        System.out.println("  ----------------------------------------");
    }

    private static void printError(String msg) {
        System.out.println("\n  ❌ Error: " + msg + "\n");
    }

    private static void printSuccess(String msg) {
        System.out.println("\n  ✔ Success: " + msg + "\n");
    }

    private static void printStatus(String msg) {
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
            } catch (NumberFormatException ignored) {}
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
        printError("Too many invalid attempts.");
        return null;
    }
}