package main.rider;

import java.util.PriorityQueue;
import main.navigation.*;

public class DeliveryManager {
    // instance variables
    PriorityQueue<Rider> availableRider;
    CityGraph<String, Double> distance;

    // constructor
    public DeliveryManager(CityGraph<String, Double> distance) {
        this.distance = distance;
        this.availableRider = new PriorityQueue<>();
    }

    // add rider method (name and distance)
    public void addRider(String name, double distance) {
        availableRider.offer(new Rider(name, distance));
    }

    // asssignBestRider method
    public void assignBestRider() {
        if (availableRider.isEmpty()) {
            System.out.println("No available rider!");
            return;
        }

        // remove the used rider (head of the priority queue)
        Rider assignedRider = availableRider.poll();
        System.out.println("Assigned: " + assignedRider);
    }
}