package main.navigation;

/**
 * Represents a specific location (node) within the city graph.
 */
public class LocationNode<T extends Comparable<T>, N extends Comparable<N>> {
    // The generic data (e.g., city name) stored in this node
    private final T info;
    // The first outgoing edge from this location
    private MapEdge<T, N> edge;
    // Number of incoming edges
    private int indegree;
    // Number of outgoing edges
    private int outdegree;
    // Pointer to the next location node in the graph's main list
    LocationNode<T, N> nextNode;

    /**
     * Initializes a new location node.
     * @param data the information to store in the node
     */
    public LocationNode(T data) {
        this.info = data;
        this.edge = null;
        this.indegree = 0;
        this.outdegree = 0;
        this.nextNode = null;
    }

    public T getInfo() {
        return info;
    }

    public MapEdge<T, N> getEdge() {
        return edge;
    }

    public int getIndegree() {
        return indegree;
    }

    public int getOutdegree() {
        return outdegree;
    }

    public void setEdge(MapEdge<T, N> edge) {
        this.edge = edge;
    }

    public void setIndegree(int indegree) {
        this.indegree = indegree;
    }

    public void setOutdegree(int outdegree) {
        this.outdegree = outdegree;
    }
    
}