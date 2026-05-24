package main.rider;

import java.util.PriorityQueue;
import main.navigation.*;

public class DeliveryManager {
    PriorityQueue<Rider> availableRider;
    CityGraph<String, Double> distance;

    public DeliveryManager(CityGraph<String, Double> distance) {
        this.distance = distance;
        this.availableRider = new PriorityQueue<>();
    }

    public String generateNextRiderId() {
        int maxId = 0;
        for (Rider r : availableRider) {
            String id = r.getRiderId();
            if (id != null && id.startsWith("DR")) {
                try {
                    int num = Integer.parseInt(id.substring(2));
                    if (num > maxId) {
                        maxId = num;
                    }
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
        return String.format("DR%03d", maxId + 1);
    }

    // add using name and distance (generate id & estimated time)
    public Rider addRider(String name, double distance) {
        String nextId = generateNextRiderId();
        Rider r = new Rider(nextId, name, distance);
        availableRider.offer(r);
        return r;
    }

    // add from CSV with explicit fields
    public void addRiderFromCsv(String riderId, String name, int estTime, double distanceKm) {
        availableRider.offer(new Rider(riderId, name, estTime, distanceKm));
    }

    public void assignBestRider() {
        if (availableRider.isEmpty()) {
            System.out.println("No available rider!");
            return;
        }
        Rider assignedRider = availableRider.poll();
        System.out.println("Assigned: " + assignedRider);
    }

    public void displayAvailableRiders() {
        System.out.println("  Available riders:");
        if (availableRider.isEmpty()) {
            System.out.println("    No riders available.");
            return;
        }
        for (Rider r : availableRider) {
            System.out.println("    " + r.toString());
        }
    }
}