package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

public class SocialNetworkGraph implements Graph {

	private Map<Integer, SocialNetworkNode> graph;
	private int numVertices;
	private int numEdges;
	private Set<SocialNetworkEdge> edges;

	public SocialNetworkGraph() {
		graph = new HashMap<Integer, SocialNetworkNode>();
		numVertices = 0;
		numEdges = 0;
		edges = new HashSet<SocialNetworkEdge>();
	}

	@Override
	public void addVertex(int num) {
		Integer item = (Integer) num;
		if (!graph.containsKey(item)) {
			SocialNetworkNode node = new SocialNetworkNode(item);
			graph.put(item, node);
			numVertices++;
		}
	}

	public void addVertex(int num, String label) {
		Integer item = (Integer) num;
		if (!graph.containsKey(item)) {
			SocialNetworkNode node = new SocialNetworkNode(item, label);
			graph.put(item, node);
			numVertices++;
		}
	}

	public void addVertex(int num, String label, Integer distance, Integer weight) {
		Integer item = (Integer) num;
		if (!graph.containsKey(item)) {
			SocialNetworkNode node = new SocialNetworkNode(item, label, distance, weight);
			graph.put(item, node);
			numVertices++;
		}
	}

	public void addNeighbor(int num1, int num2) {
		SocialNetworkNode item = graph.get(num1);
		SocialNetworkNode neighbor = graph.get(num2);

		if (item == null) {
			addVertex(num1);
			item = graph.get(num1);
		}
		if (neighbor == null) {
			addVertex(num2);
			neighbor = graph.get(num2);
		}
		addEdge(item, neighbor);
	}

	@Override
	public void addEdge(int from, int to) {
		SocialNetworkNode firstEnd = graph.get(from);
		SocialNetworkNode secondEnd = graph.get(to);

		if (firstEnd == null) {
			addVertex(from);
			firstEnd = graph.get(from);
		}
		if (secondEnd == null) {
			addVertex(to);
			secondEnd = graph.get(to);
		}
		addEdge(firstEnd, secondEnd);
	}

	public void addEdge(int from, String fromLabel, int to, String toLabel) {
		SocialNetworkNode firstEnd = graph.get(from);
		SocialNetworkNode secondEnd = graph.get(to);

		if (firstEnd == null) {
			addVertex(from, fromLabel);
			firstEnd = graph.get(from);
		}
		if (secondEnd == null) {
			addVertex(to, toLabel);
			secondEnd = graph.get(to);
		}
		addEdge(firstEnd, secondEnd);
	}

	public void addEdge(SocialNetworkEdge edge) {
		if (edges.contains(edge)) {
			System.out.println("Edge already present - " + edge);
			return;
		}

		SocialNetworkNode firstEnd = edge.getFirstEnd();
		SocialNetworkNode secondEnd = edge.getSecondEnd();
		addEdge(firstEnd, secondEnd);
	}

	public void addEdge(SocialNetworkNode firstEnd, SocialNetworkNode secondEnd) {
		if (firstEnd == null) {
			System.out.println("First end is null. Can't add edge!");
			return;
		}
		if (secondEnd == null) {
			System.out.println("First end is null. Can't add edge!");
			return;
		}
		SocialNetworkEdge edge = new SocialNetworkEdge(firstEnd, secondEnd);
		if (edges.contains(edge)) {
			System.out.println("Edge already exists - " + edge);
			return;
		}
		edges.add(edge);
		firstEnd.addNeighbor(secondEnd);
		secondEnd.addNeighbor(firstEnd);
		numEdges++;
	}

	public List<SocialNetworkNode> BFS(int start, int goal) {
		List<SocialNetworkNode> path;
		SocialNetworkNode startNode = graph.get(start);
		SocialNetworkNode endNode = graph.get(goal);
		if (startNode == null || endNode == null) {
			System.out.println("Either start node or goal node is not present. Aborting search!");
			return new LinkedList<SocialNetworkNode>();
		}
		// Find path using breadth first search
		path = findPathUsingBFS(startNode, endNode);

		if (path.isEmpty()) {
			// System.out.println("\nSorry, Goal not found!!");
		}
		return path;
	}

	protected List<SocialNetworkNode> findPathUsingBFS(SocialNetworkNode startNode, SocialNetworkNode endNode) {
		Set<SocialNetworkNode> visited = new HashSet<SocialNetworkNode>();
		Queue<SocialNetworkNode> toExplore = new LinkedList<SocialNetworkNode>();
		Map<SocialNetworkNode, SocialNetworkNode> parentMap = new HashMap<SocialNetworkNode, SocialNetworkNode>();
		System.out.println("\nRunning BFS......");
		System.out.println("Start - " + startNode);
		System.out.println("Goal - " + endNode);
		System.out.println();

		// do bfs
		boolean found = false;
		toExplore.add(startNode);

		while (!toExplore.isEmpty()) {
			SocialNetworkNode currentNode = toExplore.remove();
			System.out.println("\ntoExplore - " + toExplore);
			System.out.println("currentNode - " + currentNode);

			if (currentNode.equals(endNode)) {
				System.out.println("\nMATCH FOUND!");
				found = true;
				break;
			}

			Set<SocialNetworkNode> neighbors = currentNode.getNeighbors();
			System.out.println("found - " + found);
			System.out.println("visited - " + visited);
			System.out.println("visiting neighbors - " + neighbors);

			for (SocialNetworkNode neighbor : neighbors) {
				System.out.println("checking neighbor - " + neighbor);
				if (!visited.contains(neighbor)) {
					parentMap.put(neighbor, currentNode);
					visited.add(neighbor);
					toExplore.add(neighbor);
				} else {
					System.out.println("neighbor already visited - " + neighbor);
				}
			}
			System.out.println("toExplore - " + toExplore);
		}
		if (!found) {
			return new LinkedList<SocialNetworkNode>();
		}

		return constructPathFromParentMap(startNode, endNode, parentMap);
	}

	private List<SocialNetworkNode> constructPathFromParentMap(SocialNetworkNode startNode, SocialNetworkNode endNode,
			Map<SocialNetworkNode, SocialNetworkNode> parentMap) {
		LinkedList<SocialNetworkNode> path = new LinkedList<SocialNetworkNode>();
		SocialNetworkNode currentNode = endNode;
		path.add(currentNode);
		while (currentNode != startNode) {
			currentNode = parentMap.get(currentNode);
			path.addFirst(currentNode);
		}
		return path;
	}

	public Set<SocialNetworkEdge> getEdges() {
		return edges;
	}

	public void removeEdge(SocialNetworkEdge edgeToRemove) {
		System.out.println("Removing edge - " + edgeToRemove);
		if (!edges.contains(edgeToRemove)) {
			System.out.println("Edge not found");
			return;
		}
		SocialNetworkNode firstEnd = graph.get(edgeToRemove.getFirstEnd().getItem());
		SocialNetworkNode secondEnd = graph.get(edgeToRemove.getSecondEnd().getItem());
		if (firstEnd == null || secondEnd == null) {
			System.out.println("Either one or both end of the edge are not present. Not deleting!");
			return;
		}

		edges.remove(edgeToRemove);
		firstEnd.removeNeighbor(secondEnd);
		secondEnd.removeNeighbor(firstEnd);
	}

	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		HashMap<Integer, HashSet<Integer>> graphHashMap = new HashMap<Integer, HashSet<Integer>>();
		for (Map.Entry<Integer, SocialNetworkNode> entry : graph.entrySet()) {
			Integer nodeItem = entry.getKey();
			SocialNetworkNode node = entry.getValue();
			Set<SocialNetworkNode> neighborNodes = node.getNeighbors();

			HashSet<Integer> neighborNodeItems = new HashSet<Integer>();
			for (SocialNetworkNode neighborNode : neighborNodes) {
				neighborNodeItems.add(neighborNode.getItem());
			}
			graphHashMap.put(nodeItem, neighborNodeItems);
		}
		return graphHashMap;
	}

	public String getStringifiedGraph() {
		String result = "\n\t";
		for (Map.Entry<Integer, SocialNetworkNode> entry : graph.entrySet()) {
			SocialNetworkNode node = entry.getValue();
			result += node;
			result += " -> ";
			result += node.getNeighbors();
			result += "\n	";
		}
		return result + "\n";
	}

	public boolean checkNodeForExistence(int num) {
		SocialNetworkNode node = graph.get(num);

		return node != null;
	}

	public String toString() {
		String s = "";
		s += "\n  # Vertices - " + graph.size();
		s += "\n  # Edges - " + edges.size();
		s += "\n  Edges - " + getStringifiedEdgesList();
		s += "\n  Nodes - " + graph.values();
		s += "\n  Graph - " + getStringifiedGraph();
		return s;
	}

	public String getStringifiedEdgesList() {
		String s = "{";
		for (SocialNetworkEdge edge : edges) {
			s += edge + ", ";
		}
		s += "}";
		return s;
	}

	public int getNumVertices() {
		return numVertices;
	}

	public int getNumEdges() {
		return numEdges;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;

		final HashMap<Integer, HashSet<Integer>> thisGraph = exportGraph();
		final HashMap<Integer, HashSet<Integer>> otherGraph = ((CapGraph) other).exportGraph();

		return doGraphsMatch(thisGraph, otherGraph);
	}

	@Override
	public int hashCode() {
		int result = 1;
		HashMap<Integer, HashSet<Integer>> thisGraph = exportGraph();
		for (Entry<Integer, HashSet<Integer>> entry : thisGraph.entrySet()) {
			Integer node = entry.getKey();
			result = 31 * result + node.hashCode();
			HashSet<Integer> neighbors = entry.getValue();
			for (Integer neighbor : neighbors) {
				result = 31 * result + neighbor.hashCode();
			}
		}
		return result;
	}

	public HashSet<SocialNetworkNode> getNodes() {
		// TODO Auto-generated method stub
		return (HashSet) graph.values();
	}

	@Override
	public Graph getEgonet(int center) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Graph> getSCCs() {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean doGraphsMatch(HashMap<Integer, HashSet<Integer>> first,
			HashMap<Integer, HashSet<Integer>> second) {
		// Verify size
		if (first.size() != second.size()) {
			return false;
		}

		for (Map.Entry<Integer, HashSet<Integer>> entry : first.entrySet()) {
			Integer node = entry.getKey();
			HashSet<Integer> neighbors = entry.getValue();
			// Verify that every node and every edge matches
			if (!second.containsKey(node) || !second.get(node).equals(neighbors)) {
				return false;
			}
		}
		return true;
	}
}
