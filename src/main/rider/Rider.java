package main.rider;

public class Rider implements Comparable<Rider> {
    // instance variables
    private String name;
    private double distance;

    public Rider(String name, double distance) {
        this.name = name;
        this.distance = distance;
    }

    @Override
    public int compareTo(Rider r) {
        return Double.compare(this.distance, r.distance); // Double ensure no casting to int when compare
    }

    @Override
    public String toString() {
        return ("The rider is " + distance + " km away.");
    }

    public String getName() {
        return name;
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

}