
class MapEdge<T extends Comparable<T>, N extends Comparable<N>> {
    private LocationNode<T, N> destination;
    private int weight;
    MapEdge<T, N> nextEdge;

    public MapEdge(LocationNode<T, N> destination, int weight) {
        this.destination = destination;
        this.weight = weight;
        this.nextEdge = null;
    }
    
}
