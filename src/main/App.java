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

        printHeader("WELCOME TO SMART FOOD DELIVERY SYSTEM");

        while (true) {
            if (currentUser == null) {
                guestMenu();
            } else {
                mainMenu();
            }
        }
    }

    private static void guestMenu() {
        printHeader("HOME");
        System.out.println("  1. Login");
        System.out.println("  2. Sign up");
        System.out.println("  0. Exit");
        System.out.println("-----------------------------------------");
        String input = scanner.nextLine().trim();
        switch (input) {
            case "1" -> login();
            case "2" -> signup();
            case "0" -> exitApp();
            default -> System.out.println("Invalid choice. Enter 1, 2, or 0.");
        }
    }

    private static void mainMenu() {
        printHeader("MAIN MENU — " + currentUser.getUsername());
        System.out.println("  1. View restaurants & menus");
        System.out.println("  2. Search food");
        System.out.println("  3. Search restaurant");
        System.out.println("  4. Place order");
        System.out.println("  5. Manage order (undo / confirm)");
        System.out.println("  6. Shortest delivery path to home");
        System.out.println("  7. Process next order (admin) — pending: " + orderService.getPendingCount());
        System.out.println("  8. Log out");
        System.out.println("  0. Exit");
        System.out.println("-----------------------------------------");
        String input = scanner.nextLine().trim();
        switch (input) {
            case "1" -> viewRestaurants();
            case "2" -> searchFood();
            case "3" -> searchRestaurant();
            case "4" -> startOrdering();
            case "5" -> manageOrder();
            case "6" -> viewPath();
            case "7" -> {
                orderService.processNextOrder();
                pause();
            }
            case "8" -> {
                currentUser = null;
                System.out.println("You have been logged out.");
                pause();
            }
            case "0" -> exitApp();
            default -> System.out.println("Invalid choice. Pick a number from the menu.");
        }
    }

    private static void exitApp() {
        System.out.println("Thank you for using Smart Food Delivery System.");
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

        System.out.println("Loaded " + restaurants.size() + " restaurants and city map.");
    }

    private static void addFood(Restaurant r, FoodItem item) {
        r.addFoodItem(item);
        searchService.addFoodToMenu(item);
    }

    private static void login() {
        printHeader("LOGIN");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Welcome back, " + currentUser.getUsername() + ".");
                pause();
                return;
            }
        }
        System.out.println("Invalid username or password.");
        pause();
    }

    private static void signup() {
        printHeader("SIGN UP");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            pause();
            return;
        }
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                System.out.println("That username is already taken.");
                pause();
                return;
            }
        }
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();

        int newId = users.isEmpty() ? 1 : users.stream().mapToInt(User::getUserID).max().orElse(0) + 1;
        User newUser = new User(newId, username, password, email, phone, address);
        users.add(newUser);
        saveUser(newUser);
        System.out.println("Account created. You can log in now.");
        pause();
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
            // First run: no users file yet
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
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    private static void viewRestaurants() {
        printHeader("RESTAURANTS");
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant r = restaurants.get(i);
            System.out.printf("  %d. %-14s  %s  (rating %.1f)%n",
                i + 1, r.getName(), r.getLocation(), r.getRating());
        }
        System.out.println("-----------------------------------------");
        Integer choice = readOptionalInt("Menu # to open (0 = back): ", 0, restaurants.size());
        if (choice == null || choice == 0) {
            return;
        }
        Restaurant selected = restaurants.get(choice - 1);
        printHeader("MENU — " + selected.getName());
        for (FoodItem item : selected.getMenu()) {
            System.out.println("  " + item);
        }
        pause();
    }

    private static void searchFood() {
        printHeader("SEARCH FOOD");
        System.out.print("Name (letters only match the index; e.g. Big Mac or bigmac): ");
        String query = scanner.nextLine().trim();
        if (query.isEmpty()) {
            System.out.println("Empty search.");
            pause();
            return;
        }
        FoodItem found = searchService.findFood(query);
        if (found != null) {
            System.out.println("Found: " + found);
        } else {
            System.out.println("No exact match. Try the full food name as on the menu.");
        }
        pause();
    }

    private static void searchRestaurant() {
        printHeader("SEARCH RESTAURANT");
        System.out.print("Name (partial match OK): ");
        String query = scanner.nextLine().trim().toLowerCase();
        if (query.isEmpty()) {
            System.out.println("Empty search.");
            pause();
            return;
        }
        boolean found = false;
        for (Restaurant r : restaurants) {
            if (r.getName().toLowerCase().contains(query)) {
                System.out.println("  " + r);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No restaurant matched.");
        }
        pause();
    }

    private static void startOrdering() {
        if (currentUser == null) {
            System.out.println("Please log in first.");
            pause();
            return;
        }
        printHeader("PLACE ORDER");
        System.out.println("Pick a restaurant:");
        for (int i = 0; i < restaurants.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + restaurants.get(i).getName());
        }
        System.out.println("-----------------------------------------");
        Integer resChoice = readOptionalInt("Restaurant # (0 = cancel): ", 0, restaurants.size());
        if (resChoice == null || resChoice == 0) {
            return;
        }
        Restaurant selected = restaurants.get(resChoice - 1);
        orderService.createNewOrder(currentUser, selected);

        List<FoodItem> menu = selected.getMenu();
        printHeader("ADD ITEMS — " + selected.getName());
        for (int i = 0; i < menu.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + menu.get(i));
        }
        System.out.println("-----------------------------------------");
        System.out.println("Enter item numbers to add. Empty line when done.");

        while (true) {
            System.out.print("Item # (blank = done): ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }
            try {
                int itemChoice = Integer.parseInt(line);
                if (itemChoice > 0 && itemChoice <= menu.size()) {
                    OrderItem oi = new OrderItem(menu.get(itemChoice - 1), 1);
                    orderService.addItemToCart(oi);
                } else {
                    System.out.println("  Pick a number between 1 and " + menu.size() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("  Enter a number or leave blank to finish.");
            }
        }

        System.out.println("-----------------------------------------");
        orderService.printCartOverview(restaurants);
        System.out.printf("Cart total: RM %.2f%n", orderService.getCartTotal());
        System.out.println("Use \"Manage order\" to undo or confirm.");
        pause();
    }

    private static void manageOrder() {
        printHeader("MANAGE ORDER");
        orderService.printCartOverview(restaurants);
        if (!orderService.isCartEmpty()) {
            System.out.printf("Total: RM %.2f%n", orderService.getCartTotal());
        }
        System.out.println("-----------------------------------------");
        System.out.println("  1. Undo last item");
        System.out.println("  2. Confirm order");
        System.out.println("  0. Back");
        Integer choice = readOptionalInt("Choice: ", 0, 2);
        if (choice == null || choice == 0) {
            return;
        }
        if (choice == 1) {
            orderService.undoLastItem();
            pause();
            return;
        }
        int rid = orderService.getCartRestaurantId();
        if (rid < 0) {
            System.out.println("Cart is empty. Add items under Place order first.");
            pause();
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
            System.out.println("Could not match cart to a restaurant.");
            pause();
            return;
        }
        int orderId = (int) (System.currentTimeMillis() % 900000) + 100000;
        orderService.confirmOrder(orderId, currentUser, r);
        pause();
    }

    private static void viewPath() {
        printHeader("DELIVERY PATH → USER HOME");
        for (int i = 0; i < restaurants.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + restaurants.get(i).getName());
        }
        System.out.println("-----------------------------------------");
        Integer choice = readOptionalInt("Restaurant # (0 = back): ", 0, restaurants.size());
        if (choice == null || choice == 0) {
            return;
        }
        String resName = restaurants.get(choice - 1).getName();
        if (!map.hasLocation(resName)) {
            System.out.println("That restaurant is not on the city map.");
            pause();
            return;
        }
        double distance = map.getShortestDistance(resName, "User Home");
        List<String> path = map.getShortestPath(resName, "User Home");
        if (distance >= Double.MAX_VALUE / 2 || path.isEmpty()) {
            System.out.println("No route found to User Home.");
        } else {
            System.out.printf("Shortest distance: %.2f km%n", distance);
            System.out.println("Path: " + String.join(" → ", path));
        }
        pause();
    }

    // --- CLI helpers ---

    private static void printHeader(String title) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("  " + title);
        System.out.println("========================================");
    }

    private static void pause() {
        System.out.println("-----------------------------------------");
        System.out.print("Press Enter to continue... ");
        scanner.nextLine();
    }

    /**
     * @return null if input invalid after retries, or chosen value in [min, max]
     */
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
            System.out.println("  Enter a whole number between " + min + " and " + max + ".");
        }
        System.out.println("  Too many invalid tries. Returning.");
        return null;
    }
}
