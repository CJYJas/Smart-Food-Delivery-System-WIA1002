package main.rider;

import main.dataStructure.MyPriorityQueue;
import main.navigation.*;

/**
 * Manages available delivery riders and coordinates priority-based rider assignments.
 */
public class DeliveryManager {
    MyPriorityQueue<Rider> availableRider;
    CityGraph<String, Double> distance;

    /**
     * Constructs a manager instances with a defined city distance mapping graph.
     */
    public DeliveryManager(CityGraph<String, Double> distance) {
        this.distance = distance;
        this.availableRider = new MyPriorityQueue<>();
    }

    /**
     * Scans active riders to find the highest numeric ID and increments it for the next unique ID.
     */
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

    /**
     * Creates and enqueues a new rider with a auto-generated ID and auto-calculated estimated time.
     */
    public Rider addRider(String name, double distance) {
        String nextId = generateNextRiderId();
        Rider r = new Rider(nextId, name, distance);
        availableRider.offer(r);
        return r;
    }

    /**
     * Directly inserts a rider record containing explicit fields parsed from a CSV file.
     */
    public void addRiderFromCsv(String riderId, String name, int estTime, double distanceKm) {
        availableRider.offer(new Rider(riderId, name, estTime, distanceKm));
    }

    /**
     * Extracts and assigns the highest priority rider currently available from the queue.
     */
    public void assignBestRider() {
        if (availableRider.isEmpty()) {
            System.out.println("No available rider!");
            return;
        }
        Rider assignedRider = availableRider.poll();
        System.out.println("Assigned: " + assignedRider);
    }

    /**
     * Prints an ordered list of all riders currently waiting in the priority queue.
     */
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