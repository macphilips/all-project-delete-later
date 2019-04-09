/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 *         A class which represents a graph of geographic locations Nodes in the
 *         graph are intersections between
 *
 */

public class MapGraph {
	private int numVertices;
	private int numEdges;
	private Map<GeographicPoint, Node> map;

	/**
	 * Create a new empty MapGraph
	 */
	public MapGraph() {
		map = new HashMap<>();
	}

	/**
	 * Get the number of vertices (road intersections) in the graph
	 * 
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices() {

		return numVertices;
	}

	/**
	 * Return the intersections, which are the vertices in this graph.
	 * 
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices() {

		return map.keySet();
	}

	/**
	 * Get the number of road segments in the graph
	 * 
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges() {

		return numEdges;
	}

	/**
	 * Add a node corresponding to an intersection at a Geographic Point If the
	 * location is already in the graph or null, this method does not change the
	 * graph.
	 * 
	 * @param location
	 *            The location of the intersection
	 * @return true if a node was added, false if it was not (the node was
	 *         already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location) {

		if (location != null && !doesPointExist(location)) {
			Node node = new Node(location);
			map.put(location, node);
			this.numVertices++;
			return true;
		}
		return false;
	}

	/**
	 * Adds a directed edge to the graph from pt1 to pt2. Precondition: Both
	 * GeographicPoints have already been added to the graph
	 * 
	 * @param from
	 *            The starting point of the edge
	 * @param to
	 *            The ending point of the edge
	 * @param roadName
	 *            The name of the road
	 * @param roadType
	 *            The type of the road
	 * @param length
	 *            The length of the road, in km
	 * @throws IllegalArgumentException
	 *             If the points have not already been added as nodes to the
	 *             graph, if any of the arguments is null, or if the length is
	 *             less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName, String roadType, double length)
			throws IllegalArgumentException {
		// for debugging
		if (from == null) {
			throw new IllegalArgumentException("From cannot be NULL");
		}
		if (to == null) {
			throw new IllegalArgumentException("To cannot be NULL");
		}
		if (roadName == null) {
			throw new IllegalArgumentException("roadName cannot be NULL");
		}
		if (roadType == null) {
			throw new IllegalArgumentException("roadType cannot be NULL");
		}
		if (length < 0) {
			throw new IllegalArgumentException("length cannot be less than 0");
		}

		if (!doesPointExist(from)) {
			throw new IllegalArgumentException(String.valueOf(from) + " does not exist on the map");
		}

		if (!doesPointExist(to)) {
			throw new IllegalArgumentException(String.valueOf(to) + " does not exist on the map");
		}

		Node node = map.get(from);
		
		Edge edge = new Edge();
		edge.setLength(length);
		edge.setRoadType(roadType);
		edge.setRoadName(roadName);
		edge.setEdgePoint(to);
		node.addNeighbor(edge);
		
		this.numEdges++;

	}

	// helper method to check if the geographic point exist in the graph
	private boolean doesPointExist(GeographicPoint point) {
		return map.containsKey(point);
	}

	/**
	 * Find the path from start to goal using breadth first search
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *         path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		Consumer<GeographicPoint> temp = (x) -> {
		};
		return bfs(start, goal, temp);
	}

	//
	private Node getNode(GeographicPoint edgePoint) {

		return map.get(edgePoint);

	}

	/**
	 * Find the path from start to goal using breadth first search
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @param nodeSearched
	 *            A hook for visualization. See assignment instructions for how
	 *            to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *         path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched) {
		// TODO: Implement this method in WEEK 2

		// Hook for visualization. See writeup.
		// nodeSearched.accept(next.getLocation());

		Node startNode = map.get(start);
		Node endNode = map.get(goal);

		if (startNode == null || endNode == null) {
			System.out.println("Start or goal node is null!  No path exists.");
			return new LinkedList<GeographicPoint>();
		}

		HashMap<Node, Node> parentMap = getSearchedPath(startNode, endNode, nodeSearched);

		return reConstructPath(parentMap, startNode, endNode);
	}

	private List<GeographicPoint> reConstructPath(HashMap<Node, Node> parentMap, Node startNode, Node endNode) {
		LinkedList<Node> path = new LinkedList<Node>();
		Node curr = endNode;
		while (curr != startNode) {
			path.addFirst(curr);
			curr = parentMap.get(curr);
		}
		path.addFirst(startNode);
		List<GeographicPoint> points = new ArrayList<>();
		for (Node node : path) {
			points.add(node.getNode());
		}

		return points;
	}

	private HashMap<Node, Node> getSearchedPath(Node startNode, Node endNode, Consumer<GeographicPoint> nodeSearched) {
		
		HashSet<Node> visited = new HashSet<Node>();
		Queue<Node> toExplore = new LinkedList<Node>();
		HashMap<Node, Node> parentMap = new HashMap<Node, Node>();

		toExplore.add(startNode);

		boolean found = false;

		while (!toExplore.isEmpty()) {

			Node curr = toExplore.remove();

			if (curr == endNode) {
				found = true;
				break;
			}
			nodeSearched.accept(curr.getNode());

			List<Edge> neighbors = curr.getNeighbors();

			ListIterator<Edge> it = neighbors.listIterator(neighbors.size());

			while (it.hasPrevious()) {
				Node next = getNode(it.previous().edgePoint);

				if (!visited.contains(next)) {
					visited.add(next);
					parentMap.put(next, curr);
					toExplore.add(next);
				}
			}
		}

		if (found) {
			return parentMap;
		} else {
			System.out.println("No path exists");
			return null;
		}
	}

	/**
	 * Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @return The list of intersections that form the shortest path from start
	 *         to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
		Consumer<GeographicPoint> temp = (x) -> {
		};
		return dijkstra(start, goal, temp);
	}

	/**
	 * Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @param nodeSearched
	 *            A hook for visualization. See assignment instructions for how
	 *            to use it.
	 * @return The list of intersections that form the shortest path from start
	 *         to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched) {

		HashMap<GeographicPoint, Node> mapCopy = this.getMapCopy();

		Node startNode = mapCopy.get(start);
		startNode.setWeight(0);
		Node endNode = mapCopy.get(goal);

		if (startNode == null || endNode == null) {
			System.out.println("Start or goal node is null!  No path exists.");
			return new LinkedList<GeographicPoint>();
		}

		HashSet<Node> visited = new HashSet<Node>();

		PriorityQueue<PriorityNode> toExplore = new PriorityQueue<PriorityNode>();

		HashMap<Node, Node> parentMap = new HashMap<Node, Node>();
		PriorityNode p = new PriorityNode(startNode, 0);
		toExplore.add(p);
		boolean found = false;

		int counter = 0;
		while (!toExplore.isEmpty()) {
			PriorityNode currP = toExplore.remove();
			Node currNode = currP.getNode();
			if (!visited.contains(currNode)) {
				visited.add(currNode);
				counter++;
				if (currNode == endNode) {
					found = true;
					break;
				}

				nodeSearched.accept(currNode.getNode());
				List<Edge> neighbors = currNode.getNeighbors();

				for (Edge nextEdge : neighbors) {

					Node nextNode = mapCopy.get(nextEdge.edgePoint);

					if (!visited.contains(nextNode)) {

						double weight = currP.getPriorityLevel() + nextEdge.getLength();

						if (weight < nextNode.getWeight() || nextNode.getWeight() < 0) {
							nextNode.setWeight(weight);
							nextNode.setWeight(weight);
							parentMap.put(nextNode, currNode);
							toExplore.add(new PriorityNode(nextNode, weight));
						}
					}
				}
			}

		}

		System.out.println("Dijkstra's counted: " + counter + " nodes");

		if (!found) {
			return null;
		}

		return reConstructPath(parentMap, startNode, endNode);
	}

	private HashMap<GeographicPoint, Node> getMapCopy() {
		HashMap<GeographicPoint, Node> mapCopy = new HashMap<>(map);
		Set<GeographicPoint> set = mapCopy.keySet();
		for (GeographicPoint point : set) {
			Node node = mapCopy.get(point);
			node.setWeight(-1);
		}
		return mapCopy;
	}

	/**
	 * Find the path from start to goal using A-Star search
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @return The list of intersections that form the shortest path from start
	 *         to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		Consumer<GeographicPoint> temp = (x) -> {
		};
		return aStarSearch(start, goal, temp);
	}

	/**
	 * Find the path from start to goal using A-Star search
	 * 
	 * @param start
	 *            The starting location
	 * @param goal
	 *            The goal location
	 * @param nodeSearched
	 *            A hook for visualization. See assignment instructions for how
	 *            to use it.
	 * @return The list of intersections that form the shortest path from start
	 *         to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched) {
		int counter = 0;
		HashMap<GeographicPoint, Node> mapCopy = this.getMapCopy();

		Node startNode = mapCopy.get(start);
		startNode.setWeight(0);

		Node endNode = mapCopy.get(goal);

		if (startNode == null || endNode == null) {
			System.out.println("Start or goal node is null!  No path exists.");
			return new LinkedList<GeographicPoint>();
		}

		HashSet<Node> visited = new HashSet<Node>();

		PriorityQueue<PriorityNode> toExplore = new PriorityQueue<PriorityNode>();

		HashMap<Node, Node> parentMap = new HashMap<Node, Node>();
		PriorityNode p = new PriorityNode(startNode, 0);
		toExplore.add(p);
		boolean found = false;

		while (!toExplore.isEmpty()) {
			PriorityNode currP = toExplore.remove();
			Node currNode = currP.getNode();

			if (!visited.contains(currNode)) {
				visited.add(currNode);
				counter++;
				if (currNode == endNode) {
					found = true;
					break;
				}

				nodeSearched.accept(currNode.getNode());

				List<Edge> neighbors = currNode.getNeighbors();

				for (Edge nextEdge : neighbors) {
					Node nextNode = mapCopy.get(nextEdge.edgePoint);

					double distancefromEndNode = endNode.getNode().distance(nextNode.getNode());
					double distancefromStartNode = currP.getPriorityLevel() + nextEdge.getLength();

					if (!visited.contains(nextNode)) {
						if (distancefromStartNode < nextNode.getWeight() || nextNode.getWeight() < 0) {
							nextNode.setWeight(distancefromStartNode);
							parentMap.put(nextNode, currNode);
							toExplore.add(new PriorityNode(nextNode, distancefromStartNode)
									.setDistanceToEnd(distancefromEndNode));
						}

					}
				}
			}

		}

		System.out.println("A_STAR counted: " + counter + " nodes");
		if (!found) {
			return null;
		}

		return reConstructPath(parentMap, startNode, endNode);
	}

	public static void main(String[] args) {
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);

		List<GeographicPoint> route = theMap.dijkstra(start, end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start, end);
	}

	class Node {
		private double weight = -1;

		// Represents the geographic coordinates of this node
		private GeographicPoint node;
		// Stores list of neighbors of this node
		private List<Edge> neighbors;

		@Override
		public String toString() {
			return "Node [weight=" + weight + ", node=" + node + "";
		}

		public double getWeight() {
			return weight;
		}

		public void setWeight(double weight) {
			this.weight = weight;
		}

		public List<Edge> getNeighbors() {
			return neighbors;
		}

		public void setNeighbors(List<Edge> edges) {
			this.neighbors = edges;
		}

		public Node(GeographicPoint vertex) {
			this.node = vertex;
			neighbors = new ArrayList<>();
		}

		public void addNeighbor(Edge edge) {
			if (edge != null) {
				neighbors.add(edge);

			}
		}

		public GeographicPoint getNode() {
			return node;
		}

		public void setNode(GeographicPoint node) {
			this.node = node;
		}
	}

	class Edge {
		private String roadName;
		private String roadType;
		private double length;
		// represent the neighbor's geographic coordinates of a Node
		private GeographicPoint edgePoint;

		@Override
		public String toString() {

			return "Edge [roadName=" + roadName + ", roadType=" + roadType + ", length=" + length + ", edgePoint="
					+ edgePoint + "]";
		}

		public GeographicPoint getEdgePoint() {
			return edgePoint;
		}

		public void setEdgePoint(GeographicPoint neighbor) {
			this.edgePoint = neighbor;
		}

		public String getRoadName() {
			return roadName;
		}

		public void setRoadName(String streetName) {
			this.roadName = streetName;
		}

		public String getRoadType() {
			return roadType;
		}

		public void setRoadType(String roadType) {
			this.roadType = roadType;
		}

		public double getLength() {
			return length;
		}

		public void setLength(double length) {
			this.length = length;
		}

		public Edge() {

		}
	}

	class PriorityNode implements Comparable<PriorityNode> {
		Node node;
		double priorityLevel;
		double distanceFromStart;

		public double getDistanceFromStart() {
			return distanceFromStart;
		}

		public PriorityNode setDistanceFromStart(double distanceFromStart) {
			this.distanceFromStart = distanceFromStart;
			return this;
		}

		double distanceToEnd;

		public double getDistanceToEnd() {
			return distanceToEnd;
		}

		public PriorityNode setDistanceToEnd(double distance) {
			this.distanceToEnd = distance;
			return this;
		}

		public PriorityNode(Node node, double priority) {
			this.node = node;
			this.priorityLevel = priority;
		}

		public Node getNode() {
			return node;
		}

		public PriorityNode setNode(Node node) {
			this.node = node;
			return this;
		}

		public double getPriorityLevel() {
			return priorityLevel;
		}

		public PriorityNode setPriorityLevel(double priority) {
			this.priorityLevel = priority;
			return this;
		}

		@Override
		public int compareTo(PriorityNode arg1) {

			if ((priorityLevel + distanceToEnd) < (arg1.getPriorityLevel() + arg1.getDistanceToEnd())) {
				return -1;
			} else if ((priorityLevel + distanceToEnd) > (arg1.getPriorityLevel() + arg1.getDistanceToEnd())) {
				return 1;
			}
			return 0;
		}

	}
}
