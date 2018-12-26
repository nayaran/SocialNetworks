/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import graph.Graph;

/**
 * @author narayan.
 * 
 *         For the warm up assignment, you must implement your Graph in a class
 *         named CapGraph. Here is the stub file.
 *
 */
public class CapGraph implements Graph {

	private Map<Integer, ArrayList<Integer>> graphAdjList;
	private int numVertices;
	private int numEdges;

	public CapGraph() {
		graphAdjList = new HashMap<Integer, ArrayList<Integer>>();
		numVertices = 0;
		numEdges = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		// TODO Auto-generated method stub
		if (!graphAdjList.containsKey(num)) {
			ArrayList<Integer> neighbors = new ArrayList<Integer>();
			graphAdjList.put(num, neighbors);
			numVertices++;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		// TODO Auto-generated method stub
		graphAdjList.get(from).add(to);
		numEdges++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		// TODO Auto-generated method stub
		Graph egonet = new CapGraph();

		Integer node = Integer.valueOf(center);
		List<Integer> neighbors = graphAdjList.get(node);

		// Populate egonet for node
		egonet.addVertex(node);
		for (Integer neighbor : neighbors) {
			// Add neighbors
			egonet.addVertex(neighbor);
			egonet.addEdge(node, neighbor);
			egonet.addEdge(neighbor, node);

			// Populate other eligible edges
			// Add only those neighbors of the neighbor who are nodes' neighbors
			for (Integer neighborOfNeighor : graphAdjList.get(neighbor)) {
				if (neighbors.contains(neighborOfNeighor)) {
					egonet.addEdge(neighbor, neighborOfNeighor);
				}
			}
		}
		return egonet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		/*
		 * Core Algorithm Step 1 - Run DFS on G and store the order of traversal Step 2
		 * - Get G` (transpose of G) Step 3 - Run DFS in reverse order of traversal in
		 * Step 1
		 */

		// Initialize
		List<Graph> listOfSCCs = new ArrayList<Graph>();
		List<Stack<Integer>> listOfStackOfNodesInSCCs = new ArrayList<Stack<Integer>>();

		// Step 1 - Run DFS on G and store the order of traversal
		Stack<Integer> initialDFS = DFS(getGraphVertices(), this, listOfStackOfNodesInSCCs);
		//System.out.println("Initial DFS - " + initialDFS);

		// Step 2 - Get G` (transpose of G)
		Graph transposedGraph = getTransposedGraph();

		// Step 3 - Run DFS in reverse order of traversal in Step 1
		listOfStackOfNodesInSCCs = new ArrayList<Stack<Integer>>();
		Stack<Integer> finalDFS = DFS(initialDFS, transposedGraph, listOfStackOfNodesInSCCs);

		// Post processing
		listOfSCCs = constructSCCsfromNodes(listOfStackOfNodesInSCCs);

		//System.out.println("List of list of nodes in SCC - " + listOfStackOfNodesInSCCs);
		//System.out.println("List of SCCs - " + listOfSCCs);
		return listOfSCCs;
	}

	public Stack<Integer> DFS(Stack<Integer> vertices, Graph graph, List<Stack<Integer>> listOfListOfNodesInSCCs) {
		//System.out.println("\n\nRunning DFS on - " + graph);
		//System.out.println("  Vertices to visit - " + vertices);

		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();

		while (!vertices.isEmpty()) {
			Integer vertex = vertices.pop();

			if (!visited.contains(vertex)) {
				Stack<Integer> exploredVerticesInThisRun = new Stack<Integer>();
				DFSVisit(vertex, visited, finished, graph, exploredVerticesInThisRun);
				listOfListOfNodesInSCCs.add(exploredVerticesInThisRun);

				//System.out.println("Finished - " + finished);
				//System.out.println("Explored vertices - " + exploredVerticesInThisRun);
			}
		}
		return finished;
	}

	public void DFSVisit(Integer vertex, HashSet<Integer> visited, Stack<Integer> finished, Graph graph,
			Stack<Integer> exploredVerticesInThisRun) {
		visited.add(vertex);
		HashSet<Integer> neighbors = graph.exportGraph().get(vertex);

		//System.out.println("\nVisiting - " + vertex);
		//System.out.println("Neighbors of " + vertex + " - " + neighbors);
		//System.out.println("Visited - " + visited);

		for (Integer neighbor : neighbors) {
			if (!visited.contains(neighbor)) {
				visited.add(neighbor);
				DFSVisit(neighbor, visited, finished, graph, exploredVerticesInThisRun);
			}
		}
		finished.add(vertex);
		exploredVerticesInThisRun.push(vertex);
		//System.out.println("Done with " + vertex + " - no more unvisited neighbors");
		//System.out.println("Adding to finished and exploredVerticesInThisRun- " + vertex);
		//System.out.println("Finished - " + finished);
		//System.out.println("Explored vertices in this run - " + exploredVerticesInThisRun);
	}

	private List<Graph> constructSCCsfromNodes(List<Stack<Integer>> listOfStackOfNodesInSCCs) {
		List<Graph> listOfSCCs = new ArrayList<Graph>();
		for (Stack<Integer> stackOfNodes : listOfStackOfNodesInSCCs) {
			listOfSCCs.add(constructGraphFromStackOfNodes(stackOfNodes));
		}
		return listOfSCCs;
	}

	private Graph constructGraphFromStackOfNodes(Stack<Integer> stackOfNodes) {
		Graph graph = new CapGraph();
		ArrayList<Integer> listOfNodes = new ArrayList<Integer>(stackOfNodes);
		while (!stackOfNodes.isEmpty()) {
			Integer node = stackOfNodes.pop();
			graph.addVertex(node);
			for (Integer neighbor : graphAdjList.get(node)) {
				if (listOfNodes.contains(neighbor)) {
					graph.addEdge(node, neighbor);
				}
			}
		}
		return graph;
	}

	public Graph getTransposedGraph() {
		Graph graph = new CapGraph();

		for (Map.Entry<Integer, ArrayList<Integer>> entry : graphAdjList.entrySet()) {
			Integer node = entry.getKey();
			graph.addVertex(node);
			for (Integer neighbor : entry.getValue()) {
				graph.addVertex(neighbor);
				graph.addEdge(neighbor, node);
			}
		}
		return graph;
	}

	private Stack<Integer> getGraphVertices() {
		Stack<Integer> vertices = new Stack<Integer>();
		for (Integer vertex : graphAdjList.keySet()) {
			vertices.push(vertex);
		}
		return vertices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		// TODO Auto-generated method stub
		HashMap<Integer, HashSet<Integer>> graph = new HashMap<Integer, HashSet<Integer>>();
		for (Map.Entry<Integer, ArrayList<Integer>> entry : graphAdjList.entrySet()) {
			graph.put(entry.getKey(), new HashSet<Integer>(entry.getValue()));
		}
		return graph;
	}

	public String toString() {
		String s = "";
		s += "\n  (Vertices - " + graphAdjList.size();
		s += ", Graph - " + exportGraph() + ")";
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
		for (Map.Entry<Integer, HashSet<Integer>> entry : thisGraph.entrySet()) {
			Integer node = entry.getKey();
			result = 31 * result + node.hashCode();
			HashSet<Integer> neighbors = entry.getValue();
			for (Integer neighbor : neighbors) {
				result = 31 * result + neighbor.hashCode();
			}
		}
		return result;
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
