package main.navigation;

/**
 * Represents a directed path (edge) between two locations with an associated weight (distance).
 */
public class MapEdge<T extends Comparable<T>, N extends Comparable<N>> {
    // The target location this edge points to
    private final LocationNode<T, N> destination;
    // The distance or cost associated with this edge
    private final Double weight;
    // Pointer to the next edge originating from the same source node
    public MapEdge<T, N> nextEdge;

    /**
     * Creates a new edge connecting to a destination with a given weight.
     * @param destination the target node
     * @param weight the cost/distance of the edge
     */
    public MapEdge(LocationNode<T, N> destination, Double weight) {
        this.destination = destination;
        this.weight = weight;
        this.nextEdge = null;
    }

    public LocationNode<T, N> getDestination() {
        return destination;
    }

    public Double getWeight() {
        return weight;
    }
}
