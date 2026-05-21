package main.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import main.model.User;

public class UserManager {
    private List<User> users;
    private HashMap<String, User> userMap;

    public UserManager() {
        this.users = new ArrayList<>();
        this.userMap = new HashMap<>();
    }

    public void addUser(User user) {
        users.add(user);
        userMap.put(user.getUsername(), user);
        System.out.println("User added: " + user.getUsername());
    }

    public void removeUser(int userID) {
        users.removeIf(user -> user.getUserID() == userID);
        userMap.remove(Integer.toString(userID));
        System.out.println("User removed: " + userID);
    }

    public void displayUser() {
        System.out.println("Users:");
        for (User user : users) {
            System.out.println(user);
        }
    }

    public User searchUser(String username) {
        return userMap.get(username);
    }

}