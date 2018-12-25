/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import graph.Graph;
/**
 * @author Anurag.
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
		return null;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		// TODO Auto-generated method stub
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
		String s = "Adjacency list representation\n";
		s += "Number of vertices - " + graphAdjList.size();

		for (int vertex : graphAdjList.keySet()) {
			s += "\n\t"+vertex+": ";
			for (int edge : graphAdjList.get(vertex)) {
				s += edge+", ";
			}
		}
		return s;
	}

	public int getNumVertices() {
		return numVertices;
	}

	public int getNumEdges() {
		return numEdges;
	}
}
