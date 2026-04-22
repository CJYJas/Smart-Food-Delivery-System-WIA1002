class CityGraph<T extends Comparable<T>, N extends Comparable<N>> {
    private LocationNode<T, N> head;

    public CityGraph() {
        this.head = null;
    }

    public void addLocation(T location) {
        LocationNode<T, N> newNode = new LocationNode<>(location);
        if (head == null) {
            head = newNode;
        } else {
            LocationNode<T, N> current = head;
            while (current.nextNode != null) {
                current = current.nextNode;
            }
            current.nextNode = newNode;
        }
    }

    public void addRoad(T from, T to, int weight) {
        LocationNode<T, N> fromNode = findLocation(from);
        LocationNode<T, N> toNode = findLocation(to);
        if (fromNode != null && toNode != null) {
            MapEdge<T, N> newEdge = new MapEdge<>(toNode, weight);
            if (fromNode.getEdge() == null) {
                fromNode.setEdge(newEdge);
            } else {
                MapEdge<T, N> currentEdge = fromNode.getEdge();
                while (currentEdge.nextEdge != null) {
                    currentEdge = currentEdge.nextEdge;
                }
                currentEdge.nextEdge = newEdge;
            }
            fromNode.setOutdegree(fromNode.getOutdegree() + 1);
            toNode.setIndegree(toNode.getIndegree() + 1);
        }
    }

    private LocationNode<T, N> findLocation(T location) {
        LocationNode<T, N> current = head;
        while (current != null) {
            if (current.getInfo().equals(location)) {
                return current;
            }
            current = current.nextNode;
        }
        return null;
    }
    
}
