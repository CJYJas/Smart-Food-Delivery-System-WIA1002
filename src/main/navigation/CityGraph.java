package main.navigation;

import java.util.*;

/**
 * Represents a graph of city locations and the roads connecting them.
 */
public class CityGraph<T extends Comparable<T>, N extends Comparable<N>> {
    // The first node in the adjacency list representing the graph
    private LocationNode<T, N> head;

    /**
     * Initializes an empty city graph.
     */
    public CityGraph() {
        this.head = null;
    }

    /**
     * Adds a new location (vertex) to the graph.
     * @param location the identifier of the new location
     */
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

    /**
     * Adds a directed road (edge) between two existing locations.
     * @param from the starting location
     * @param to the destination location
     * @param weight the distance or cost of the road
     */
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

    private final Map<LocationNode<T, N>, LocationNode<T, N>> pathTracker = new HashMap<>();

    /**
     * Calculates the shortest distance between two locations using Dijkstra's algorithm.
     * @param from the starting location
     * @param to the destination location
     * @return the shortest distance, or Double.MAX_VALUE if no path exists
     */
    public double getShortestDistance(T from, T to) {
        LocationNode<T, N> fromNode = findLocation(from);
        LocationNode<T, N> toNode = findLocation(to);

        if (fromNode == null || toNode == null) {
            return Double.MAX_VALUE;
        }

        pathTracker.clear();
        Map<LocationNode<T, N>, Double> distances = new HashMap<>();
        PriorityQueue<LocationDistance<T, N>> pq = new PriorityQueue<>();

        distances.put(fromNode, 0.0);
        pq.add(new LocationDistance<>(fromNode, 0.0));

        while (!pq.isEmpty()) {
            LocationDistance<T, N> current = pq.poll();
            LocationNode<T, N> currentNode = current.location;

            // Ignore outdated queue entries (same node was later reached with a shorter distance)
            if (current.totalDistance > distances.getOrDefault(currentNode, Double.MAX_VALUE)) {
                continue;
            }

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

    /**
     * Retrieves the sequence of locations forming the shortest path.
     * @param from the starting location
     * @param to the destination location
     * @return a list of locations representing the path
     */
    public List<T> getShortestPath(T from, T to) {
        LocationNode<T, N> fromNode = findLocation(from);
        LocationNode<T, N> toNode = findLocation(to);
        if (fromNode == null || toNode == null) {
            return new ArrayList<>();
        }

        if (getShortestDistance(from, to) == Double.MAX_VALUE) {
            return new ArrayList<>();
        }

        List<T> path = new ArrayList<>();
        LocationNode<T, N> current = toNode;
        while (current != null) {
            path.add(0, current.getInfo());
            if (current.equals(fromNode)) {
                break;
            }
            current = pathTracker.get(current);
        }
        if (path.isEmpty() || !path.get(0).equals(from)) {
            return new ArrayList<>();
        }
        return path;
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