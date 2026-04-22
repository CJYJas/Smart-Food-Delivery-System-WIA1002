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

    public void addRoad(T from, T to, double  weight) {
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

    public boolean hasLocation(T location) {
        return findLocation(location) != null;
    }

    public boolean hasRoad(T from, T to) {
        LocationNode<T, N> fromNode = findLocation(from);
        if (fromNode != null) {
            MapEdge<T, N> currentEdge = fromNode.getEdge();
            while (currentEdge != null) {
                if (currentEdge.getDestination().getInfo().equals(to)) {
                    return true;
                }
                currentEdge = currentEdge.nextEdge;
            }
        }
        return false;
    }

    public LocationNode<T, N> getNeigbours(T location) {
        LocationNode<T, N> node = findLocation(location);
        if (node != null) {
            return node;
        }
        return null;
    }

    public MapEdge<T, N> getShortestPath(T from, T to) {
        LocationNode<T, N> fromNode = findLocation(from);
        LocationNode<T, N> toNode = findLocation(to);
        if (fromNode == null || toNode == null) {
            return null;
        }
        // Implement Dijkstra's algorithm or any shortest path algorithm here
        // This is a placeholder for the actual implementation
        return null;
    }

    public Double  getShortestDistance(T from, T to) {
        MapEdge<T, N> shortestPath = getShortestPath(from, to);
        if (shortestPath != null) {
            return shortestPath.getWeight();
        }
        return Double.MAX_VALUE;
    }

    public void printGraph() {
        LocationNode<T, N> current = head;
        while (current != null) {
            System.out.print(current.getInfo() + " -> ");
            MapEdge<T, N> currentEdge = current.getEdge();
            while (currentEdge != null) {
                System.out.print(currentEdge.getDestination().getInfo() + "(" + currentEdge.getWeight() + ") ");
                currentEdge = currentEdge.nextEdge;
            }
            System.out.println();
            current = current.nextNode;
        }
    }
    
}
