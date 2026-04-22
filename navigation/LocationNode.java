class LocationNode<T extends Comparable<T>, N extends Comparable<N>> {
    private final T info;
    private MapEdge<T, N> edge;
    private int indegree;
    private int outdegree;
    LocationNode<T, N> nextNode;

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
