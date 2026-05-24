package main.model;

import java.io.*;
import java.util.*;
import main.App;
import main.user.UserManager;

public class User {
    private int userID;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;

    public User(int userID, String username, String password, String email, String phone, String address) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("[ID: %d] %-10s | %-20s | %-15s | %-30s", userID, username, email, phone, address);
    }

    public static User login(Scanner scanner, UserManager userManager) {
        App.printHeader("USER LOGIN");
        System.out.print(" 👤 Username: ");
        String username = scanner.nextLine().trim();
        System.out.print(" 🔑 Password: ");
        String password = scanner.nextLine();

        User user = userManager.searchUser(username);
        if (user != null && user.getPassword().equals(password)) {
            App.printSuccess("Login successful! Welcome back, " + user.getUsername() + ".");
            return user;
        }
        App.printError("Access denied. Invalid credentials.");
        return null;
    }

    public static void signup(Scanner scanner, List<User> users, UserManager userManager) {
        App.printHeader("SIGN UP");
        System.out.print(" 👤 Username: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            App.printError("Username cannot be empty.");
            return;
        }
        if (userManager.searchUser(username) != null) {
            App.printError("Username already exists.");
            return;
        }
        System.out.print("  Password: ");
        String password = scanner.nextLine();
        System.out.print("  Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("  Contact Number: ");
        String phone = scanner.nextLine().trim();
        System.out.print("  Address: ");
        String address = scanner.nextLine().trim();

        int newId = users.isEmpty() ? 1 : users.stream().mapToInt(User::getUserID).max().orElse(0) + 1;
        User newUser = new User(newId, username, password, email, phone, address);
        users.add(newUser);
        userManager.addUser(newUser);
        saveUser(newUser);
        App.printSuccess("Account created successfully.");
    }

    public static void loadUsers(List<User> users, UserManager userManager) {
        try (Scanner fileScanner = new Scanner(new File(App.USERS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    User u = new User(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4],
                        parts[5]
                    );
                    users.add(u);
                    userManager.addUser(u);
                }
            }
        } catch (FileNotFoundException e) {
            // Safe fallback
        }
    }

    public static void saveUser(User user) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(App.USERS_FILE, true)))) {
            out.println(String.format("%d|%s|%s|%s|%s|%s",
                user.getUserID(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress()
            ));
        } catch (IOException e) {
            App.printError("Storage Write Fault: " + e.getMessage());
        }
    }
}