package main;

import java.util.List;

import main.model.*;
import main.search.SearchService;

public class AdminMenu {
    static void adminLogin() {
        App.printHeader("ADMIN LOGIN");
        System.out.print("  Admin password: ");
        String pw = App.scanner.nextLine().trim();
        if (!pw.equals("admin")) {
            App.printError("Wrong admin password.");
            return;
        }
        App.printSuccess("Welcome, Admin!");
        adminMenu();
    }

    private static void adminMenu() {
        boolean inAdmin = true;
        while (inAdmin) {
            App.printHeader("ADMIN DASHBOARD");
            System.out.println("  1. Manage Users");
            System.out.println("  2. Manage Restaurants");
            System.out.println("  3. View All Menu Items (BST In-Order)");
            System.out.println("  4. Search Food Item (Trie)");
            System.out.println("  5. View City Map (Graph)");
            System.out.println("  6. Find Shortest Path (Dijkstra)");
            System.out.println("  7. Manage Riders & Delivery");
            System.out.println("  8. Process Next Order (Queue)");
            System.out.println("  9. View Pending Order Count");
            System.out.println("  0. Logout");
            App.printDivider();
            System.out.print("  Select option: ");
            String ch = App.scanner.nextLine().trim();

            switch (ch) {
                case "1" -> manageUsers();
                case "2" -> manageRestaurants();
                case "3" -> {
                    App.printHeader("ALL MENU ITEMS (BST In-Order)");
                    App.searchService.showMenu();
                }
                case "4" -> searchFood();
                case "5" -> {
                    App.printHeader("CITY MAP (Adjacency List)");
                    App.cityGraph.printGraph();
                }
                case "6" -> shortestPath();
                case "7" -> manageRiders();
                case "8" -> {
                    App.printHeader("PROCESS NEXT ORDER");
                    App.orderService.processNextOrder();
                }
                case "9" -> {
                    App.printHeader("PENDING ORDERS");
                    System.out.println("  Orders in queue: " + App.orderService.getPendingCount());
                }
                case "0" -> {
                    inAdmin = false;
                    App.printSuccess("Admin logged out.");
                }
                default -> App.printError("Invalid option.");
            }
        }
    }

    private static void manageUsers() {
        boolean loop = true;
        while (loop) {
            App.printHeader("MANAGE USERS");
            System.out.println("  1. View All Users");
            System.out.println("  2. Search User by Username");
            System.out.println("  3. Remove User by ID");
            System.out.println("  0. Back");
            App.printDivider();
            System.out.print("  Select option: ");
            String ch = App.scanner.nextLine().trim();

            switch (ch) {
                case "1" -> {
                    App.printHeader("ALL USERS");
                    if (App.users.isEmpty()) {
                        System.out.println("  No users registered.");
                    } else {
                        System.out.printf("  %-4s %-15s %-20s %-15s %-30s%n",
                                "ID", "Username", "Email", "Phone", "Address");
                        App.printDivider();
                        for (User u : App.users) {
                            System.out.println(u);
                        }
                    }
                    
                }
                case "2" -> {
                    System.out.print("  Enter username: ");
                    String name = App.scanner.nextLine().trim();
                    User found = App.userManager.searchUser(name);
                    if (found != null) {
                        App.printSuccess("User found:");
                        System.out.println("  " + found);
                    } else {
                        App.printError("User '" + name + "' not found.");
                    }
                    
                }
                case "3" -> {
                    System.out.print("  Enter User ID to remove: ");
                    try {
                        int id = Integer.parseInt(App.scanner.nextLine().trim());
                        boolean removed = App.userManager.removeUser(id);
                        if (removed) {
                            App.users.removeIf(u -> u.getUserID() == id);
                        }
                    } catch (NumberFormatException e) {
                        App.printError("Invalid ID.");
                    }
                    
                }
                case "0" -> loop = false;
                default -> App.printError("Invalid option.");
            }
        }
    }

    private static void manageRestaurants() {
        boolean loop = true;
        while (loop) {
            App.printHeader("MANAGE RESTAURANTS");
            System.out.println("  1. View All Restaurants");
            System.out.println("  2. Add Restaurant");
            System.out.println("  3. Remove Restaurant by ID");
            System.out.println("  4. View Restaurant Menu");
            System.out.println("  0. Back");
            App.printDivider();
            System.out.print("  Select option: ");
            String ch = App.scanner.nextLine().trim();

            switch (ch) {
                case "1" -> {
                    App.printHeader("ALL RESTAURANTS");
                    App.restaurantManager.displayRestaurant();
                    
                }
                case "2" -> {
                    System.out.print("  Restaurant ID: ");
                    int rid = Integer.parseInt(App.scanner.nextLine().trim());
                    System.out.print("  Name: ");
                    String rname = App.scanner.nextLine().trim();
                    System.out.print("  Location: ");
                    String rloc = App.scanner.nextLine().trim();
                    System.out.print("  Rating: ");
                    double rrate = Double.parseDouble(App.scanner.nextLine().trim());
                    Restaurant nr = new Restaurant(rid, rname, rloc, rrate);
                    App.restaurants.add(nr);
                    App.restaurantManager.addRestaurant(nr);
                    
                }
                case "3" -> {
                    System.out.print("  Enter Restaurant ID to remove: ");
                    try {
                        int id = Integer.parseInt(App.scanner.nextLine().trim());
                        boolean removed = App.restaurantManager.removeRestaurant(id);
                        if (removed) {
                            App.restaurants.removeIf(r -> r.getRestaurantID() == id);
                        }
                    } catch (NumberFormatException e) {
                        App.printError("Invalid ID.");
                    }
                    
                }
                case "4" -> {
                    System.out.print("  Enter Restaurant ID: ");
                    try {
                        int id = Integer.parseInt(App.scanner.nextLine().trim());
                        Restaurant r = App.restaurantManager.searchRestaurant(id);
                        if (r != null) {
                            App.printHeader("MENU - " + r.getName());
                            if (r.getMenu().isEmpty()) {
                                System.out.println("  No items.");
                            } else {
                                for (FoodItem fi : r.getMenu()) {
                                    System.out.println("  " + fi);
                                }
                            }
                        } else {
                            App.printError("Restaurant not found.");
                        }
                    } catch (NumberFormatException e) {
                        App.printError("Invalid ID.");
                    }
                    
                }
                case "0" -> loop = false;
                default -> App.printError("Invalid option.");
            }
        }
    }

    private static void searchFood() {
        App.printHeader("SEARCH FOOD ITEM (Trie)");
        System.out.print("  Enter food name: ");
        String query = App.scanner.nextLine().trim();
        FoodItem result = App.searchService.findFood(query);
        if (result != null) {
            App.printSuccess("Found:");
            System.out.println("  " + result);
        } else {
            App.printError("'" + query + "' not found in menu.");
        }
        
    }

    private static void shortestPath() {
        App.printHeader("SHORTEST PATH (Dijkstra)");
        System.out.print("  From: ");
        String from = App.scanner.nextLine().trim();
        System.out.print("  To:   ");
        String to = App.scanner.nextLine().trim();

        if (!App.cityGraph.hasLocation(from)) {
            App.printError("Location '" + from + "' not found on map.");
            
            return;
        }
        if (!App.cityGraph.hasLocation(to)) {
            App.printError("Location '" + to + "' not found on map.");
            
            return;
        }

        double dist = App.cityGraph.getShortestDistance(from, to);
        if (dist == Double.MAX_VALUE) {
            App.printError("No path exists between '" + from + "' and '" + to + "'.");
        } else {
            List<String> path = App.cityGraph.getShortestPath(from, to);
            App.printSuccess(String.format("Shortest distance: %.1f km", dist));
            System.out.println("  Route: " + String.join(" -> ", path));
        }
        
    }

    private static void manageRiders() {
        boolean loop = true;
        while (loop) {
            App.printHeader("MANAGE RIDERS & DELIVERY");
            System.out.println("  1. Add Rider");
            System.out.println("  2. Assign Best Rider (closest)");
            System.out.println("  0. Back");
            App.printDivider();
            System.out.print("  Select option: ");
            String ch = App.scanner.nextLine().trim();

            switch (ch) {
                case "1" -> {
                    System.out.print("  Rider name: ");
                    String name = App.scanner.nextLine().trim();
                    System.out.print("  Distance (km): ");
                    double d = Double.parseDouble(App.scanner.nextLine().trim());
                    App.deliveryManager.addRider(name, d);
                    App.printSuccess("Rider '" + name + "' added.");
                    
                }
                case "2" -> {
                    App.printHeader("ASSIGN BEST RIDER");
                    App.deliveryManager.assignBestRider();
                    
                }
                case "0" -> loop = false;
                default -> App.printError("Invalid option.");
            }
        }
    }
}
