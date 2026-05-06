import model.*;
import navigation.*;
import order.*;
import search.*;
import rider.*;
import java.util.*;
import java.io.*;

public class App {
    private static final String USERS_FILE = "users.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderService orderService = new OrderService();
    private static final SearchService searchService = new SearchService();
    private static final CityGraph<String, Integer> map = new CityGraph<>();
    //private static final BST foodSearch = new BST();
    
    private static User currentUser;
    private static final List<User> users = new ArrayList<>();
    private static final List<Restaurant> restaurants = new ArrayList<>();

    public static void main(String[] args) {
        initializeData();
        loadUsers();

        System.out.println("===========================================");
        System.out.println("   WELCOME TO SMART FOOD DELIVERY SYSTEM   ");
        System.out.println("===========================================");

        while (true) {
            if (currentUser == null) {
                System.out.println("\n1. Login");
                System.out.println("2. Signup");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                
                String input = scanner.nextLine();
                switch (input) {
                    case "1" -> login();
                    case "2" -> signup();
                    case "0" -> {
                        System.out.println("Thank you for using Smart Food Delivery System!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } else {
                System.out.println("\n--- MAIN MENU ---");
                System.out.println("1. View Restaurants & Menus");
                System.out.println("2. Search Food");
                System.out.println("3. Search Restaurant");
                System.out.println("4. Place Order");
                System.out.println("5. Manage Current Order (Undo/Confirm)");
                System.out.println("6. View Shortest Delivery Path");
                System.out.println("7. Process Next Order (Admin)");
                System.out.println("8. Logout");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                
                String input = scanner.nextLine();
                switch (input) {
                    case "1" -> viewRestaurants();
                    case "2" -> searchFood();
                    case "3" -> searchRestaurant();
                    case "4" -> startOrdering();
                    case "5" -> manageOrder();
                    case "6" -> viewPath();
                    case "7" -> orderService.processNextOrder();
                    case "8" -> {
                        currentUser = null;
                        System.out.println("Logged out successfully.");
                    }
                    case "0" -> {
                        System.out.println("Thank you for using Smart Food Delivery System!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }

    private static void initializeData() {
        // Initialize Map
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

        // Initialize Restaurants & Food
        
        // 1. McD
        Restaurant mcd = new Restaurant(1, "McD", "McD Street", 4.5);
        addFood(mcd, new FoodItem(101, "Big Mac", 15.50, "Burger", 1));
        addFood(mcd, new FoodItem(102, "Fries", 6.00, "Side", 1));
        addFood(mcd, new FoodItem(103, "McChicken", 12.00, "Burger", 1));
        restaurants.add(mcd);

        // 2. KFC
        Restaurant kfc = new Restaurant(2, "KFC", "KFC Avenue", 4.3);
        addFood(kfc, new FoodItem(201, "Zinger Burger", 14.50, "Burger", 2));
        addFood(kfc, new FoodItem(202, "2-pc Combo", 18.00, "Chicken", 2));
        addFood(kfc, new FoodItem(203, "Whipped Potato", 5.50, "Side", 2));
        restaurants.add(kfc);

        // 3. Pizza Hut
        Restaurant pizzaHut = new Restaurant(3, "Pizza Hut", "Pizza Plaza", 4.2);
        addFood(pizzaHut, new FoodItem(301, "Pepperoni Pizza", 25.00, "Pizza", 3));
        addFood(pizzaHut, new FoodItem(302, "Garlic Bread", 8.00, "Side", 3));
        addFood(pizzaHut, new FoodItem(303, "Spaghetti Carbonara", 16.50, "Pasta", 3));
        restaurants.add(pizzaHut);

        // 4. Subway
        Restaurant subway = new Restaurant(4, "Subway", "Central Mall", 4.6);
        addFood(subway, new FoodItem(401, "Italian BMT", 14.90, "Sandwich", 4));
        addFood(subway, new FoodItem(402, "Roasted Chicken", 13.50, "Sandwich", 4));
        addFood(subway, new FoodItem(403, "Chocolate Chip Cookie", 2.50, "Dessert", 4));
        restaurants.add(subway);

        // 5. Sushi King
        Restaurant sushiKing = new Restaurant(5, "Sushi King", "Central Mall", 4.4);
        addFood(sushiKing, new FoodItem(501, "Salmon Sushi", 6.00, "Sushi", 5));
        addFood(sushiKing, new FoodItem(502, "Ebi Tempura", 12.00, "Side", 5));
        addFood(sushiKing, new FoodItem(503, "Chicken Teriyaki Don", 15.00, "Main", 5));
        restaurants.add(sushiKing);

        System.out.println("System data initialized successfully.");
    }

    private static void addFood(Restaurant r, FoodItem item) {
        r.addFoodItem(item);
        searchService.addFoodToMenu(item);
    }

    private static void login() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("Login successful! Welcome, " + currentUser.getUsername());
                return;
            }
        }
        System.out.println("Invalid username or password.");
    }

    private static void signup() {
        System.out.println("\n--- SIGNUP ---");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        int newId = users.size() + 1;
        User newUser = new User(newId, username, password, email, phone, address);
        users.add(newUser);
        saveUser(newUser);
        System.out.println("Signup successful! You can now login.");
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
            System.out.println("Users file not found. Starting with empty user list.");
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
        System.out.println("\n--- RESTAURANTS ---");
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant r = restaurants.get(i);
            System.out.println((i + 1) + ". " + r.getName() + " (" + r.getLocation() + ") - Rating: " + r.getRating());
        }
        System.out.print("Enter restaurant number to see menu (0 to go back): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= restaurants.size()) {
                Restaurant selected = restaurants.get(choice - 1);
                System.out.println("\n--- MENU FOR " + selected.getName() + " ---");
                for (FoodItem item : selected.getMenu()) {
                    System.out.println(item);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void searchFood() {
        System.out.println("\n--- SEARCH FOOD ---");
        System.out.print("Enter food name: ");
        String query = scanner.nextLine();
        FoodItem found = searchService.findFood(query);
        if (found != null) {
            System.out.println("Found: " + found);
        } else {
            System.out.println("Food item not found.");
        }
    }

    private static void searchRestaurant() {
        System.out.println("\n--- SEARCH RESTAURANT ---");
        System.out.print("Enter restaurant name: ");
        String query = scanner.nextLine();
        boolean found = false;
        for (Restaurant r : restaurants) {
            if (r.getName().equalsIgnoreCase(query)) {
                System.out.println("Found: " + r);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Restaurant not found.");
        }
    }

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
        System.out.println("Select a Restaurant:");
        for (int i = 0; i < restaurants.size(); i++) {
            System.out.println((i + 1) + ". " + restaurants.get(i).getName());
        }
        System.out.print("Choice: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= restaurants.size()) {
                String resName = restaurants.get(choice - 1).getName();
                if (map.hasLocation(resName)) {
                    double distance = map.getShortestDistance(resName, "User Home");
                    List<String> path = map.getShortestPath(resName, "User Home");
                    System.out.println("\nShortest distance to Home: " + distance + " km");
                    System.out.println("Path: " + String.join(" -> ", path));
                } else {
                    System.out.println("Restaurant location not found on map.");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
}