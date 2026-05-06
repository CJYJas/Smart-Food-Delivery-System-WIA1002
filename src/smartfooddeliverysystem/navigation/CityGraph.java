package navigation;

import java.util.*;

public class CityGraph<T extends Comparable<T>, N extends Comparable<N>> {
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

    private final Map<LocationNode<T, N>, LocationNode<T, N>> pathTracker = new HashMap<>();

    public List<T> getShortestPath(T from, T to) {
        List<T> path = new ArrayList<>();
        LocationNode<T, N> current = findLocation(to);
        while (current != null) {
            path.add(0, current.getInfo());
            current = pathTracker.get(current);
        }
        return path;
    }

    public Double  getShortestDistance(T from, T to) {
        LocationNode<T, N> fromNode = findLocation(from);
        LocationNode<T, N> toNode = findLocation(to);

        if (fromNode == null || toNode == null) {
            return Double.MAX_VALUE;
        }

        Map<LocationNode<T, N>, Double> distances = new HashMap<>();
        PriorityQueue<LocationDistance<T, N>> pq = new PriorityQueue<>();

        distances.put(fromNode, 0.0);
        pq.add(new LocationDistance<>(fromNode, 0.0));

        while (!pq.isEmpty()) {
            LocationDistance<T, N> current = pq.poll();
            LocationNode<T, N> currentNode = current.location;

            if (currentNode.equals(toNode)) {
                return current.totalDistance;
            }

            MapEdge<T, N> edge = currentNode.getEdge();
            while (edge != null) {
                LocationNode<T, N> neighbor = edge.getDestination();
                double weight = edge.getWeight();
                double newDist = current.totalDistance + weight;

                if (newDist < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    pathTracker.put(neighbor, currentNode);
                    pq.add(new LocationDistance<>(neighbor, newDist));
                }
                edge = edge.nextEdge;
            }
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
