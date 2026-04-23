
public class MapEdge<T extends Comparable<T>, N extends Comparable<N>> {
    private final LocationNode<T, N> destination;
    private final Double weight;
    public MapEdge<T, N> nextEdge;

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
