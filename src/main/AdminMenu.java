package main;

import java.util.List;

import main.model.*;

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
            System.out.println("  3. View City Map (Graph)");
            System.out.println("  4. Find Shortest Path (Dijkstra)");
            System.out.println("  5. Manage Riders & Delivery");
            System.out.println("  6. Process Next Order (Queue)");
            System.out.println("  7. View Pending Order Count");
            System.out.println("  0. Logout");
            App.printDivider();
            System.out.print("  Select option: ");
            String ch = App.scanner.nextLine().trim();

            switch (ch) {
                case "1" -> manageUsers();
                case "2" -> manageRestaurants();
                case "3" -> {
                    App.printHeader("CITY MAP (Adjacency List)");
                    App.cityGraph.printGraph();
                }
                case "4" -> shortestPath();
                case "5" -> manageRiders();
                case "6" -> {
                    App.printHeader("PROCESS NEXT ORDER");
                    App.orderService.processNextOrder();
                }
                case "7" -> {
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
            System.out.println("  5. Add Menu Item");
            System.out.println("  6. Update Menu Item");
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
                    String rid = generateNextRestaurantId();
                    System.out.println("  Generated Restaurant ID: " + rid);
                    System.out.print("  Name: ");
                    String rname = App.scanner.nextLine().trim();
                    if (rname.isEmpty()) {
                        App.printError("Restaurant name cannot be empty.");
                        break;
                    }
                    System.out.print("  Location: ");
                    String rloc = App.scanner.nextLine().trim();
                    if (rloc.isEmpty()) {
                        App.printError("Restaurant location cannot be empty.");
                        break;
                    }
                    System.out.print("  Rating: ");
                    double rrate;
                    try {
                        rrate = Double.parseDouble(App.scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        App.printError("Invalid rating. Restaurant was not added.");
                        break;
                    }
                    Restaurant nr = new Restaurant(rid, rname, rloc, rrate);
                    App.restaurants.add(nr);
                    App.restaurantManager.addRestaurant(nr);
                    persistRestaurant(nr);
                    App.printSuccess("Restaurant '" + rname + "' added with ID " + rid + ".");
                }
                case "3" -> {
                    System.out.print("  Enter Restaurant ID to remove: ");
                    String id = App.scanner.nextLine().trim();
                    if (id.isEmpty()) {
                        App.printError("Invalid ID.");
                        break;
                    }
                    boolean removed = App.restaurantManager.removeRestaurant(id);
                    if (removed) {
                        App.restaurants.removeIf(r -> r.getRestaurantID().equals(id));
                        App.allFood.removeIf(fi -> fi.getRestaurantID().equals(id));
                        App.searchService.rebuildMenu(App.allFood);
                    }
                }
                case "4" -> viewRestaurantMenu();
                case "5" -> addRestaurantMenuItem();
                case "6" -> updateRestaurantMenuItem();
                case "0" -> loop = false;
                default -> App.printError("Invalid option.");
            }
        }
    }

    private static void viewRestaurantMenu() {
        App.printHeader("VIEW RESTAURANT MENU");
        System.out.print("  Enter Restaurant ID: ");
        String id = App.scanner.nextLine().trim();
        if (id.isEmpty()) {
            App.printError("Invalid ID.");
            return;
        }
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
    }

    private static void addRestaurantMenuItem() {
        App.printHeader("ADD MENU ITEM");
        System.out.print("  Enter Restaurant ID: ");
        String rid = App.scanner.nextLine().trim();
        Restaurant r = App.restaurantManager.searchRestaurant(rid);
        if (r == null) {
            App.printError("Restaurant not found.");
            return;
        }

        try {
            int itemId = generateNextMenuItemId(rid);
            System.out.println("  Generated Item ID: " + itemId);

            System.out.print("  Name: ");
            String name = App.scanner.nextLine().trim();
            if (name.isEmpty()) {
                App.printError("Menu item name cannot be empty.");
                return;
            }
            System.out.print("  Price: ");
            double price = Double.parseDouble(App.scanner.nextLine().trim());
            System.out.print("  Category: ");
            String category = App.scanner.nextLine().trim();
            if (category.isEmpty()) {
                App.printError("Category cannot be empty.");
                return;
            }

            FoodItem newItem = new FoodItem(itemId, name, price, category, rid);
            r.addFoodItem(newItem);
            App.allFood.add(newItem);
            App.searchService.addFoodToMenu(newItem);
            persistMenuItem(newItem);
            App.printSuccess("Menu item '" + name + "' added to " + r.getName() + " with ID " + itemId + ".");
        } catch (NumberFormatException e) {
            App.printError("Invalid input. Menu item was not added.");
        }
    }

    private static void updateRestaurantMenuItem() {
        App.printHeader("UPDATE MENU ITEM");
        System.out.print("  Enter Restaurant ID: ");
        String rid = App.scanner.nextLine().trim();
        Restaurant r = App.restaurantManager.searchRestaurant(rid);
        if (r == null) {
            App.printError("Restaurant not found.");
            return;
        }

        if (r.getMenu().isEmpty()) {
            App.printError("This restaurant has no menu items to update.");
            return;
        }

        System.out.println("  Current menu items:");
        for (FoodItem fi : r.getMenu()) {
            System.out.println("  " + fi);
        }
        App.printDivider();
        System.out.print("  Enter Item ID to update: ");
        try {
            int itemId = Integer.parseInt(App.scanner.nextLine().trim());

            FoodItem selectedItem = null;
            for (FoodItem fi : r.getMenu()) {
                if (fi.getItemID() == itemId) {
                    selectedItem = fi;
                    break;
                }
            }
            if (selectedItem == null) {
                App.printError("Menu item not found.");
                return;
            }

            System.out.println("  Leave blank to keep current value.");
            System.out.print("  New name (" + selectedItem.getName() + "): ");
            String newName = App.scanner.nextLine().trim();
            System.out.print("  New price (" + selectedItem.getPrice() + "): ");
            String newPriceText = App.scanner.nextLine().trim();
            System.out.print("  New category (" + selectedItem.getCategory() + "): ");
            String newCategory = App.scanner.nextLine().trim();

            boolean nameChanged = false;
            if (!newName.isEmpty()) {
                selectedItem.setName(newName);
                nameChanged = true;
            }
            if (!newPriceText.isEmpty()) {
                selectedItem.setPrice(Double.parseDouble(newPriceText));
            }
            if (!newCategory.isEmpty()) {
                selectedItem.setCategory(newCategory);
            }

            if (nameChanged) {
                App.searchService.addFoodToMenu(selectedItem);
            }

            App.printSuccess("Menu item updated successfully.");
        } catch (NumberFormatException e) {
            App.printError("Invalid input. Menu item was not updated.");
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
            System.out.println("  3. List Available Riders");
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
                    main.rider.Rider r = App.deliveryManager.addRider(name, d);
                    // persist to riders.csv
                    try (java.io.FileWriter fw = new java.io.FileWriter(App.RIDERS_FILE, true)) {
                        fw.write(System.lineSeparator() + r.getRiderId() + "," + r.getName() + "," + r.getEstimatedTime() + "," + r.getDistance());
                    } catch (java.io.IOException e) {
                        App.printError("Failed to persist rider: " + e.getMessage());
                    }
                    App.printSuccess("Rider '" + name + "' added.");
                }
                case "2" -> {
                    App.printHeader("ASSIGN BEST RIDER");
                    App.deliveryManager.assignBestRider();
                    
                }
                case "3" -> {
                    App.printHeader("AVAILABLE RIDERS");
                    App.deliveryManager.displayAvailableRiders();
                }
                case "0" -> loop = false;
                default -> App.printError("Invalid option.");
            }
        }
    }

    private static String generateNextRestaurantId() {
        int maxId = 0;
        for (Restaurant r : App.restaurants) {
            String id = r.getRestaurantID();
            if (id != null && id.startsWith("R")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > maxId) {
                        maxId = num;
                    }
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
        return String.format("R%03d", maxId + 1);
    }

    private static void persistRestaurant(Restaurant r) {
        try (java.io.PrintWriter out = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(App.RESTAURANTS_FILE, true)))) {
            out.println(String.format("%s,%s,%s,%.1f",
                r.getRestaurantID(),
                r.getName(),
                r.getLocation(),
                r.getRating()
            ));
        } catch (java.io.IOException e) {
            App.printError("Failed to persist restaurant: " + e.getMessage());
        }
    }

    private static int generateNextMenuItemId(String rid) {
        int restaurantNum = 1;
        if (rid != null && rid.startsWith("R")) {
            try {
                restaurantNum = Integer.parseInt(rid.substring(1));
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        int minId = restaurantNum * 100;
        int maxId = minId;
        for (FoodItem existing : App.allFood) {
            if (existing.getRestaurantID().equals(rid)) {
                if (existing.getItemID() >= minId && existing.getItemID() < minId + 100) {
                    if (existing.getItemID() > maxId) {
                        maxId = existing.getItemID();
                    }
                }
            }
        }
        return (maxId == minId) ? minId + 1 : maxId + 1;
    }

    private static void persistMenuItem(FoodItem item) {
        try (java.io.PrintWriter out = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(App.MENU_FILE, true)))) {
            out.println(String.format("%d,%s,%.2f,%s,%s",
                item.getItemID(),
                item.getName(),
                item.getPrice(),
                item.getCategory(),
                item.getRestaurantID()
            ));
        } catch (java.io.IOException e) {
            App.printError("Failed to persist menu item: " + e.getMessage());
        }
    }
}
