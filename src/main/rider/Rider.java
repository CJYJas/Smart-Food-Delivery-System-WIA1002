package main.rider;

public class Rider implements Comparable<Rider> {
    private String riderId;
    private String name;
    private int estimatedTime; // minutes
    private double distance; // km

    public Rider(String riderId, String name, double distance) {
        this.riderId = riderId;
        this.name = name;
        this.estimatedTime = Math.max(5, (int) Math.round(distance * 2));
        this.distance = distance;
    }

    public Rider(String riderId, String name, int estimatedTime, double distance) {
        this.riderId = riderId;
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.distance = distance;
    }

    @Override
    public int compareTo(Rider r) {
        int cmp = Integer.compare(this.estimatedTime, r.estimatedTime);
        if (cmp != 0) return cmp;
        return Double.compare(this.distance, r.distance);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %d min, %.1f km", riderId, name, estimatedTime, distance);
    }

    public String getRiderId() { return riderId; }
    public String getName() { return name; }
    public int getEstimatedTime() { return estimatedTime; }
    public double getDistance() { return distance; }

    public void setDistance(double distance) { this.distance = distance; }
    public void setName(String name) { this.name = name; }
    public void setEstimatedTime(int t) { this.estimatedTime = t; }
}