package main.user;

import java.util.HashMap;
import main.model.User;

/**
 * Manages the collection of users in the system.
 * Provides functionality to add, remove, search, and display users.
 * Utilizes HashMaps for efficient retrieval by user ID and username.
 */
public class UserManager {
    private HashMap<Integer, User> usersById;
    private HashMap<String, User> usersByUsername;

    /**
     * Constructs a new UserManager with empty user mappings.
     */
    public UserManager() {
        this.usersById = new HashMap<>();
        this.usersByUsername = new HashMap<>();
    }

    /**
     * Adds a new user to the manager.
     * Prevents adding a user if the username already exists.
     *
     * @param user The user to add.
     */
    public void addUser(User user) {
        if (searchUser(user.getUsername()) != null) {
            System.out.println("User with username " + user.getUsername() + " already exists.");
            return;
        }
        usersById.put(user.getUserID(), user);
        usersByUsername.put(user.getUsername(), user);
        System.out.println("User added: " + user.getUsername());
    }

    /**
     * Removes a user from the manager based on their user ID.
     *
     * @param userID The ID of the user to remove.
     * @return true if the user was successfully removed, false otherwise.
     */
    public boolean removeUser(int userID) {
        User user = usersById.remove(userID);
        if (user != null) {
            usersByUsername.remove(user.getUsername());
            System.out.println("User removed: " + user.getUsername());
            return true;
        }
        System.out.println("User with ID " + userID + " not found.");
        return false;
    }

    /**
     * Displays all users currently managed by the UserManager.
     */
    public void displayUser() {
        System.out.println("Users:");
        for (User user : usersById.values()) {
            System.out.println(user);
        }
    }

    /**
     * Retrieves a collection of all users.
     *
     * @return a Collection of all User objects.
     */
    public java.util.Collection<User> getAllUsers() {
        return usersById.values();
    }

    /**
     * Searches for a user by their username.
     *
     * @param username The username to search for.
     * @return the User object if found, null otherwise.
     */
    public User searchUser(String username) {
        return usersByUsername.get(username);
    }

    /**
     * Searches for a user by their user ID.
     *
     * @param userID The user ID to search for.
     * @return the User object if found, null otherwise.
     */
    public User searchUserById(int userID) {
        return usersById.get(userID);
    }

    /**
     * Generates a new unique user ID based on the maximum existing ID.
     *
     * @return a new, unique integer ID.
     */
    public int generateNewId() {
        return usersById.keySet().stream().max(Integer::compare).orElse(0) + 1;
    }

}