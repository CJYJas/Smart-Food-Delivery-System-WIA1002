package main.navigation;

/**
 * A helper class used to track the distance to a specific location node.
 */
public class LocationDistance<T extends Comparable<T>, N extends Comparable<N>> implements Comparable<LocationDistance<T, N>> {
    // The location node being evaluated
    LocationNode<T, N> location;
    // The accumulated distance from the starting node to this location
    double totalDistance;

    /**
     * Constructs a LocationDistance object.
     * @param location the location node
     * @param totalDistance the total distance to reach this node
     */
    public LocationDistance(LocationNode<T, N> location, double totalDistance) {
        this.location = location;
        this.totalDistance = totalDistance;
    }
    
    @Override
    public int compareTo(LocationDistance<T, N> other) {
        return Double.compare(this.totalDistance, other.totalDistance);
    }
}