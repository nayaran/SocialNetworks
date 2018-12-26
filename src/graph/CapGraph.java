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
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {

	private Map<Integer,ArrayList<Integer>> graphAdjList;
	private int numVertices;
	private int numEdges;

	public CapGraph() {
		graphAdjList = new HashMap<Integer, ArrayList<Integer>>();
		numVertices = 0;
		numEdges = 0;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		// TODO Auto-generated method stub
		if (!graphAdjList.containsKey(num)) {
			ArrayList<Integer> neighbors = new ArrayList<Integer>();
			graphAdjList.put(num,  neighbors);
			numVertices++;
		}
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		// TODO Auto-generated method stub
		graphAdjList.get(from).add(to);
		numEdges++;
	}

	/* (non-Javadoc)
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
		for (Integer neighbor:neighbors) {
			// Add neighbors
			egonet.addVertex(neighbor);
			egonet.addEdge(node, neighbor);
			egonet.addEdge(neighbor, node);

			// Populate other eligible edges
			// Add only those neighbors of the neighbor who are nodes' neighbors
			for (Integer neighborOfNeighor: graphAdjList.get(neighbor)) {
				if (neighbors.contains(neighborOfNeighor)) {
					egonet.addEdge(neighbor, neighborOfNeighor);
				}
			}
		}
		return egonet;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		return null;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		// TODO Auto-generated method stub
		HashMap<Integer, HashSet<Integer>> graph = new  HashMap<Integer, HashSet<Integer>>();
		for (Map.Entry<Integer, ArrayList<Integer>> entry : graphAdjList.entrySet()) {
			graph.put(entry.getKey(), new HashSet<Integer>(entry.getValue()));
		}
		return graph;
	}

	public String toString() {
		String s= "";
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
			for (Integer neighbor: neighbors) {
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
			if (!second.containsKey(node) ||
					!second.get(node).equals(neighbors)){
				return false;
			}
		}
		return true;
	}
}
