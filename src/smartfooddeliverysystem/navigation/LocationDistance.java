package navigation;

public class LocationDistance<T extends Comparable<T>, N extends Comparable<N>> implements Comparable<LocationDistance<T, N>> {
    LocationNode<T, N> location;
    double totalDistance;

    public LocationDistance(LocationNode<T, N> location, double totalDistance) {
        this.location = location;
        this.totalDistance = totalDistance;
    }
    
    @Override
    public int compareTo(LocationDistance<T, N> other) {
        return Double.compare(this.totalDistance, other.totalDistance);
    }
}
