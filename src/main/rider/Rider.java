package main.rider;

/**
 * Model class representing a delivery rider that sorts by estimated time and
 * absolute distance.
 */
public class Rider implements Comparable<Rider> {
    private String riderId;
    private String name;
    private int estimatedTime; // minutes
    private double distance; // km

    /**
     * Initializes a rider with a distance variable used to automatically compute
     * estimated arrival time.
     */
    public Rider(String riderId, String name, double distance) {
        this.riderId = riderId;
        this.name = name;
        this.estimatedTime = Math.max(5, (int) Math.round(distance * 2));
        this.distance = distance;
    }

    /**
     * Initializes a rider with predetermined attributes imported from static data
     * sources.
     */
    public Rider(String riderId, String name, int estimatedTime, double distance) {
        this.riderId = riderId;
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.distance = distance;
    }

    /**
     * Compares riders using estimated arrival time followed by distance as a tiebreaker.
     */
    @Override
    public int compareTo(Rider r) {
        int cmp = Integer.compare(this.estimatedTime, r.estimatedTime);
        if (cmp != 0)
            return cmp;
        return Double.compare(this.distance, r.distance);
    }

    /**
     * Formats the rider's information details into a clean string statement representation.
     */
    @Override
    public String toString() {
        return String.format("%s (%s) - %d min, %.1f km", riderId, name, estimatedTime, distance);
    }

    // getters and setters method
    public String getRiderId() {
        return riderId;
    }

    public String getName() {
        return name;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEstimatedTime(int t) {
        this.estimatedTime = t;
    }
}