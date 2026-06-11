package main.model;

import java.io.*;
import java.util.*;
import main.App;
import main.user.UserManager;

/**
 * Represents a user in the Smart Food Delivery System.
 * Contains user details such as ID, username, password, email, phone, and address.
 * Also provides static methods for user authentication and account management.
 */
public class User {
    private int userID;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;

    /**
     * Constructs a new User object.
     *
     * @param userID   The unique identifier for the user.
     * @param username The username of the user.
     * @param password The password for the user.
     * @param email    The email address of the user.
     * @param phone    The contact number of the user.
     * @param address  The physical address of the user.
     */
    public User(int userID, String username, String password, String email, String phone, String address) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    /**
     * Retrieves the user ID.
     * @return the user ID.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the user ID.
     * @param userID the new user ID.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Retrieves the username.
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * @param username the new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the user password.
     * @return the user password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user password.
     * @param password the new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the user email address.
     * @return the email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user email address.
     * @param email the new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the user contact number.
     * @return the phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the user contact number.
     * @param phone the new phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Retrieves the user physical address.
     * @return the address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the user physical address.
     * @param address the new address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns a string representation of the user.
     * @return a formatted string containing user details.
     */
    @Override
    public String toString() {
        return String.format("[ID: %d] %-10s | %-20s | %-15s | %-30s", userID, username, email, phone, address);
    }

    /**
     * Handles the user login process.
     * Prompts for username and password, then verifies against the UserManager.
     *
     * @param scanner     Scanner object to read user input.
     * @param userManager The UserManager containing the registered users.
     * @return the logged-in User object if successful, null otherwise.
     */
    public static User login(Scanner scanner, UserManager userManager) {
        App.printHeader("USER LOGIN");
        System.out.print("  Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("  Password: ");
        String password = scanner.nextLine();

        User user = userManager.searchUser(username);
        if (user != null && user.getPassword().equals(password)) {
            App.printSuccess("Login successful! Welcome back, " + user.getUsername() + ".");
            return user;
        }
        App.printError("Access denied. Invalid credentials.");
        return null;
    }

    /**
     * Handles the user registration process.
     * Prompts for user details, creates a new User object, adds it to the UserManager, and saves to file.
     *
     * @param scanner     Scanner object to read user input.
     * @param userManager The UserManager to handle the new user registration.
     */
    public static void signup(Scanner scanner, UserManager userManager) {
        App.printHeader("SIGN UP");
        System.out.print("  Username: ");
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

        int newId = userManager.generateNewId();
        User newUser = new User(newId, username, password, email, phone, address);
        userManager.addUser(newUser);
        saveUser(newUser);
        App.printSuccess("Account created successfully.");
    }

    /**
     * Loads existing users from the data file into the given UserManager.
     *
     * @param userManager The UserManager to populate with loaded users.
     */
    public static void loadUsers(UserManager userManager) {
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
                            parts[5]);
                    userManager.addUser(u);
                }
            }
        } catch (FileNotFoundException e) {
            // Safe fallback
        }
    }

    /**
     * Saves a newly created user to the data file.
     *
     * @param user The user object to save.
     */
    public static void saveUser(User user) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(App.USERS_FILE, true)))) {
            out.println(String.format("%d|%s|%s|%s|%s|%s",
                    user.getUserID(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getAddress()));
        } catch (IOException e) {
            App.printError("Storage Write Fault: " + e.getMessage());
        }
    }
}