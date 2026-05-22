package main.user;

import java.util.LinkedList;
import main.model.User;

public class UserManager {
    private LinkedList<User> users;

    public UserManager() {
        this.users = new LinkedList<>();
    }

    public void addUser(User user) {
        if(searchUser(user.getUsername()) != null) {
            System.out.println("User with username " + user.getUsername() + " already exists.");
            return;
        }
        users.add(user);
        System.out.println("User added: " + user.getUsername());
    }

    public boolean removeUser(int userID) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUserID() == userID) {
                users.remove(i);
                System.out.println("User removed: " + user.getUsername());
                return true;
            }
        }        System.out.println("User with ID " + userID + " not found.");
        return false;
    }

    public void displayUser() {
        System.out.println("Users:");
        for (User user : users) {
            System.out.println(user);
        }
    }

    public User searchUser(String username) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

}