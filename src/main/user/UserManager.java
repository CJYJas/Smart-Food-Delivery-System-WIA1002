package main.user;

import java.util.HashMap;
import main.model.User;

public class UserManager {
    private HashMap<Integer, User> usersById;
    private HashMap<String, User> usersByUsername;

    public UserManager() {
        this.usersById = new HashMap<>();
        this.usersByUsername = new HashMap<>();
    }

    public void addUser(User user) {
        if (searchUser(user.getUsername()) != null) {
            System.out.println("User with username " + user.getUsername() + " already exists.");
            return;
        }
        usersById.put(user.getUserID(), user);
        usersByUsername.put(user.getUsername(), user);
        System.out.println("User added: " + user.getUsername());
    }

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

    public void displayUser() {
        System.out.println("Users:");
        for (User user : usersById.values()) {
            System.out.println(user);
        }
    }

    public java.util.Collection<User> getAllUsers() {
        return usersById.values();
    }

    public User searchUser(String username) {
        return usersByUsername.get(username);
    }

    public User searchUserById(int userID) {
        return usersById.get(userID);
    }

    public int generateNewId() {
        return usersById.keySet().stream().max(Integer::compare).orElse(0) + 1;
    }

}