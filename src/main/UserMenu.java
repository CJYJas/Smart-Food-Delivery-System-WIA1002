package main;

import java.util.List;

import main.model.*;

public class UserMenu {

    static void userLogin() {
        User currentUser = User.login(App.scanner, App.userManager);
        if (currentUser == null) return;
        userMenu(currentUser);
    }

    private static void userMenu(User currentUser) {
        boolean inUser = true;
        while (inUser) {
            App.printHeader("USER MENU - Welcome, " + currentUser.getUsername());
            System.out.println("  1. Browse Restaurants");
            System.out.println("  2. Browse Full Menu (sorted A-Z)");
            System.out.println("  3. Search Food by Name");
            System.out.println("  4. Start New Order");
            System.out.println("  5. View Cart");
            System.out.println("  6. Undo Last Cart Item (Stack)");
            System.out.println("  7. Confirm Order (Place Order)");
            System.out.println("  8. Find Delivery Route");
            System.out.println("  9. View My Profile");
            System.out.println("  0. Logout");
            App.printDivider();
            System.out.print("  Select option: ");
            String ch = App.scanner.nextLine().trim();

            switch (ch) {
                case "1" -> browseRestaurants();
                case "2" -> {
                    App.printHeader("FULL MENU (BST In-Order)");
                    App.searchService.showMenu();
                    
                }
                case "3" -> searchFood();
                case "4" -> startOrder(currentUser);
                case "5" -> {
                    App.printHeader("YOUR CART");
                    App.orderService.printCartOverview(App.restaurants);
                    if (!App.orderService.isCartEmpty()) {
                        System.out.printf("  %-30s RM %.2f%n", "Total:", App.orderService.getCartTotal());
                    }
                    
                }
                case "6" -> {
                    App.printHeader("UNDO LAST ITEM");
                    App.orderService.undoLastItem();
                    
                }
                case "7" -> confirmOrder(currentUser);
                case "8" -> deliveryRoute(currentUser);
                case "9" -> {
                    App.printHeader("MY PROFILE");
                    System.out.println("  ID:       " + currentUser.getUserID());
                    System.out.println("  Username: " + currentUser.getUsername());
                    System.out.println("  Email:    " + currentUser.getEmail());
                    System.out.println("  Phone:    " + currentUser.getPhone());
                    System.out.println("  Address:  " + currentUser.getAddress());
                    
                }
                case "0" -> {
                    inUser = false;
                    App.printSuccess(currentUser.getUsername() + " logged out.");
                }
                default -> App.printError("Invalid option.");
            }
        }
    }

    private static void browseRestaurants() {
        App.printHeader("RESTAURANTS");
        App.restaurantManager.displayRestaurant();
        App.printDivider();
        System.out.print("  Enter Restaurant ID to view menu (or 0 to go back): ");
        try {
            int id = Integer.parseInt(App.scanner.nextLine().trim());
            if (id == 0) return;
            Restaurant r = App.restaurantManager.searchRestaurant(id);
            if (r != null) {
                App.printHeader("MENU - " + r.getName() + " (" + r.getLocation() + ")");
                if (r.getMenu().isEmpty()) {
                    System.out.println("  No items available.");
                } else {
                    for (FoodItem fi : r.getMenu()) {
                        System.out.println("  " + fi);
                    }
                }
            } else {
                App.printError("Restaurant not found.");
            }
        } catch (NumberFormatException e) {
            App.printError("Invalid input.");
        }
    }

    private static void searchFood() {
        App.printHeader("SEARCH FOOD (Trie)");
        System.out.print("  Enter food name: ");
        String query = App.scanner.nextLine().trim();
        FoodItem result = App.searchService.findFood(query);
        if (result != null) {
            App.printSuccess("Found:");
            System.out.println("  " + result);
        } else {
            App.printError("'" + query + "' not found.");
        }
    }

    private static void startOrder(User currentUser) {
        App.printHeader("NEW ORDER");
        App.restaurantManager.displayRestaurant();
        App.printDivider();
        System.out.print("  Select Restaurant ID: ");
        try {
            int rid = Integer.parseInt(App.scanner.nextLine().trim());
            Restaurant r = App.restaurantManager.searchRestaurant(rid);
            if (r == null) {
                App.printError("Restaurant not found.");
                return;
            }

            App.orderService.createNewOrder(currentUser, r);

            boolean ordering = true;
            while (ordering) {
                System.out.println();
                App.printHeader("MENU - " + r.getName());
                List<FoodItem> menu = r.getMenu();
                if (menu.isEmpty()) {
                    System.out.println("  No items available.");
                    break;
                }
                for (FoodItem fi : menu) {
                    System.out.println("  " + fi);
                }
                App.printDivider();
                System.out.println("  1. Add item to cart");
                System.out.println("  2. Undo last item (Stack pop)");
                System.out.println("  3. View cart");
                System.out.println("  4. Done adding items");
                App.printDivider();
                System.out.print("  Select option: ");
                String ch = App.scanner.nextLine().trim();

                switch (ch) {
                    case "1" -> {
                        System.out.print("  Enter Item ID: ");
                        try {
                            int itemId = Integer.parseInt(App.scanner.nextLine().trim());
                            FoodItem selected = null;
                            for (FoodItem fi : menu) {
                                if (fi.getItemID() == itemId) {
                                    selected = fi;
                                    break;
                                }
                            }
                            if (selected == null) {
                                App.printError("Item not found in this restaurant's menu.");
                            } else {
                                System.out.print("  Quantity: ");
                                int qty = Integer.parseInt(App.scanner.nextLine().trim());
                                if (qty <= 0) {
                                    App.printError("Quantity must be at least 1.");
                                } else {
                                    OrderItem oi = new OrderItem(selected, qty);
                                    App.orderService.addItemToCart(oi);
                                }
                            }
                        } catch (NumberFormatException e) {
                            App.printError("Invalid input.");
                        }
                    }
                    case "2" -> App.orderService.undoLastItem();
                    case "3" -> {
                        App.printHeader("CURRENT CART");
                        App.orderService.printCartOverview(App.restaurants);
                        if (!App.orderService.isCartEmpty()) {
                            System.out.printf("  %-25s RM %.2f%n", "Total:", App.orderService.getCartTotal());
                        }
                    }
                    case "4" -> ordering = false;
                    default -> App.printError("Invalid option.");
                }
            }
        } catch (NumberFormatException e) {
            App.printError("Invalid input.");
            
        }
    }

    private static void confirmOrder(User currentUser) {
        App.printHeader("CONFIRM ORDER");
        if (App.orderService.isCartEmpty()) {
            App.printError("Your cart is empty. Add items first.");
            
            return;
        }
        App.orderService.printCartOverview(App.restaurants);
        System.out.printf("  %-30s RM %.2f%n", "Total:", App.orderService.getCartTotal());
        App.printDivider();
        System.out.print("  Confirm order? (y/n): ");
        String confirm = App.scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("y")) {
            int rid = App.orderService.getCartRestaurantId();
            Restaurant r = App.restaurantManager.searchRestaurant(rid);
            App.orderService.confirmOrder(App.nextOrderId++, currentUser, r);
            App.printSuccess("Order placed and added to processing queue!");
        } else {
            System.out.println("  Order cancelled.");
        }
        
    }

    private static void deliveryRoute(User currentUser) {
        App.printHeader("DELIVERY ROUTE FINDER");
        System.out.println("  Your address: " + currentUser.getAddress());
        System.out.println();
        System.out.println("  Available locations on map:");
        for (Restaurant r : App.restaurants) {
            System.out.println("    - " + r.getLocation());
        }
        System.out.println("    - User Home");
        App.printDivider();
        System.out.print("  Enter restaurant location (from): ");
        String from = App.scanner.nextLine().trim();
        System.out.print("  Enter your location (to): ");
        String to = App.scanner.nextLine().trim();

        if (!App.cityGraph.hasLocation(from)) {
            App.printError("'" + from + "' is not on the city map.");
            
            return;
        }
        if (!App.cityGraph.hasLocation(to)) {
            App.printError("'" + to + "' is not on the city map.");
            
            return;
        }

        double dist = App.cityGraph.getShortestDistance(from, to);
        if (dist == Double.MAX_VALUE) {
            App.printError("No route found between '" + from + "' and '" + to + "'.");
        } else {
            List<String> path = App.cityGraph.getShortestPath(from, to);
            App.printSuccess(String.format("Shortest delivery distance: %.1f km", dist));
            System.out.println("  Route: " + String.join(" -> ", path));
            App.printDivider();
            System.out.println("  Assigning best available rider...");
            App.deliveryManager.assignBestRider();
        }
        
    }
}
