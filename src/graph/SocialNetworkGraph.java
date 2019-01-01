package graph;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class SocialNetworkGraph implements Graph {

	private Map<Integer, SocialNetworkNode> graph;
	private int numVertices;
	private int numEdges;

	public SocialNetworkGraph() {
		graph = new HashMap<Integer, SocialNetworkNode>();
		numVertices = 0;
		numEdges = 0;
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

	public void addVertex(SocialNetworkNode node) {
		Integer item = (Integer) node.getItem();
		if (!graph.containsKey(item)) {
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

	public void addEdge(int from, int to, float betweenness) {
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
		addEdge(firstEnd, secondEnd, betweenness);
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

	private void addEdge(SocialNetworkNode firstEnd, SocialNetworkNode secondEnd) {
		if (firstEnd == null) {
			System.out.println("First end is doesn't exist!");
			return;
		}
		if (secondEnd == null) {
			System.out.println("First end is null. Can't add edge!");
			return;
		}
		SocialNetworkEdge edge = new SocialNetworkEdge(firstEnd, secondEnd);

		if (firstEnd.getEdges().contains(edge) && secondEnd.getEdges().contains(edge)) {
			System.out.println("Edge already exists - " + edge);
			return;
		}
		firstEnd.addNeighbor(secondEnd);
		secondEnd.addNeighbor(firstEnd);
		numEdges++;
	}

	private void addEdge(SocialNetworkNode firstEnd, SocialNetworkNode secondEnd, float betweenness) {
		if (firstEnd == null) {
			System.out.println("First end is doesn't exist!");
			return;
		}
		if (secondEnd == null) {
			System.out.println("First end is null. Can't add edge!");
			return;
		}
		SocialNetworkEdge edge = new SocialNetworkEdge(firstEnd, secondEnd, betweenness);

		if (firstEnd.getEdges().contains(edge) && secondEnd.getEdges().contains(edge)) {
			System.out.println("Edge already exists - " + edge);
			return;
		}
		firstEnd.addNeighbor(secondEnd, betweenness);
		secondEnd.addNeighbor(firstEnd, betweenness);
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
		Set<SocialNetworkEdge> edges = new HashSet<SocialNetworkEdge>();
		for (SocialNetworkNode node : graph.values()) {
			edges.addAll(node.getEdges());
		}
		return edges;
	}

	public void removeEdge(SocialNetworkEdge edge) {
		System.out.println("Removing edge - " + edge);

		Integer firstVertex = edge.getFirstEnd().getItem();
		Integer secondVertex = edge.getSecondEnd().getItem();

		removeEdge(firstVertex, secondVertex);
	}

	public void removeEdge(int from, int to) {
		System.out.println("Removing edge between " + from + " and " + to);

		SocialNetworkNode firstEnd = graph.get(from);
		SocialNetworkNode secondEnd = graph.get(to);

		if (firstEnd == null || secondEnd == null) {
			System.out.println("Invalid edge. Either first end or second end or both ends don't exist");
			return;
		}
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
		s += "\n  # Edges - " + getEdges().size();
		s += "\n  Edges - " + getStringifiedEdgesList();
		s += "\n  Nodes - " + graph.values();
		s += "\n  Graph - " + getStringifiedGraph();
		return s;
	}

	public String getStringifiedEdgesList() {
		String s = "{";
		for (SocialNetworkEdge edge : getEdges()) {
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

		return doGraphsMatch(this, (SocialNetworkGraph) other);
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

	private Map<Integer, SocialNetworkNode> getGraph() {
		return graph;
	}

	public SocialNetworkNode getNode(Integer num) {
		SocialNetworkNode node = graph.get(num);
		if (node == null)
			System.out.println("Node doesnt exist for " + num);
		return graph.get(num);
	}

	public static boolean doGraphsMatch(SocialNetworkGraph g1, SocialNetworkGraph g2) {
		// Check nodes
		Map<Integer, SocialNetworkNode> graphA = g1.getGraph();
		Map<Integer, SocialNetworkNode> graphB = g2.getGraph();

		if (graphA.size() != graphB.size())
			return false;

		for (Map.Entry<Integer, SocialNetworkNode> entry : graphA.entrySet()) {
			Integer nodeItemOfA = entry.getKey();
			SocialNetworkNode nodeOfA = entry.getValue();
			SocialNetworkNode correspondingNodeOfB = graphB.get(nodeItemOfA);
			if (!nodeOfA.isExactlyEqual(correspondingNodeOfB))
				return false;
		}
		return true;
	}

	public SocialNetworkNode updateDistanceAndWeights(SocialNetworkNode nodeS, ArrayList<SocialNetworkNode> leafNodes) {
		System.out.println("\nRunning updateDistanceAndWeightsViaBFS......\n");

		Map<SocialNetworkNode, SocialNetworkNode> parentMap = new HashMap<SocialNetworkNode, SocialNetworkNode>();
		SocialNetworkNode lastNode;

		// Step I
		updateStartingNode(nodeS);

		// Step II
		updateNodesAdjacentToStartingNode(nodeS, parentMap);

		// Step III
		lastNode = updateRemainingNodes(nodeS, parentMap, leafNodes);

		System.out.println("\nLast node - " + lastNode);
		System.out.println("Parent map - " + parentMap);
		System.out.println("Leaf nodes - " + leafNodes);

		return lastNode;
	}

	private void updateStartingNode(SocialNetworkNode nodeS) {
		// Step I
		// Initial vertex s is given a distance
		// Ds = 0
		// Ws = 1

		System.out.println("Beginning with start node, nodeS - " + nodeS);

		Integer newDistance = 0;
		Integer newWeight = 1;
		nodeS.setDistance(0);
		nodeS.setWeight(1);

		System.out.println("\tUpdated distance - " + newDistance);
		System.out.println("\tUpdated  weight - " + newWeight);
		System.out.println("\tNode after updation -" + nodeS);

	}

	private void updateNodesAdjacentToStartingNode(SocialNetworkNode nodeS,
			Map<SocialNetworkNode, SocialNetworkNode> parentMap) {
		// Every vertex adjacent to starting node is given
		// Di = Ds + 1 = 1
		// Wi = Ws = 1

		System.out.println("nodeS neighbors - " + nodeS.getNeighbors());

		Integer newDistance;
		Integer newWeight;

		for (SocialNetworkNode nodeI : nodeS.getNeighbors()) {
			System.out.println("\nnodeI - " + nodeI);
			System.out.println("\tNode is adjacent to start node - " + nodeI);

			parentMap.put(nodeI, nodeS);

			newDistance = nodeS.getDistance() + 1;
			newWeight = nodeS.getWeight();
			nodeI.setDistance(newDistance);
			nodeI.setWeight(newWeight);

			System.out.println("\tUpdated distance - " + newDistance);
			System.out.println("\tUpdated  weight - " + newWeight);
			System.out.println("\tNode after updation -" + nodeI);
		}
	}

	private SocialNetworkNode updateRemainingNodes(SocialNetworkNode nodeS,
			Map<SocialNetworkNode, SocialNetworkNode> parentMap, ArrayList<SocialNetworkNode> leafNodes) {
		// USE BFS for exploring and updating rest of the nodes in the graph
		// Core updation algo in updateDistanceAndWeight

		Queue<SocialNetworkNode> toExplore = new LinkedList<SocialNetworkNode>();
		HashSet<SocialNetworkNode> visited = new HashSet<SocialNetworkNode>();

		Set<SocialNetworkNode> neighorsOfNodeS = nodeS.getNeighbors();

		if (neighorsOfNodeS.isEmpty()) {
			System.out.println("No neighbors to explore");
			return null;
		}
		toExplore.addAll(neighorsOfNodeS);
		visited.add(nodeS);
		visited.addAll(neighorsOfNodeS);

		System.out.println("\nStarting BFS exploration");
		SocialNetworkNode currentNode = null;
		while (!toExplore.isEmpty()) {
			System.out.println("\ntoExplore - " + toExplore);
			currentNode = toExplore.remove();
			System.out.println("currentNode - " + currentNode);

			Set<SocialNetworkNode> neighbors = currentNode.getNeighbors();
			System.out.println("Visited - " + visited);
			System.out.println("Visiting neighbors - " + neighbors);

			Integer numNeighborsAtNextLevel = 0;
			for (SocialNetworkNode neighbor : neighbors) {
				System.out.println("\tAt neighbor - " + neighbor);

				updateDistanceAndWeightForRemainingNodes(currentNode, neighbor);

				// Check if there is any node whose shortest path from start node
				// goes through current node
				if (neighbor.getDistance() > currentNode.getDistance())
					numNeighborsAtNextLevel++;

				if (!visited.contains(neighbor)) {
					parentMap.put(neighbor, currentNode);
					visited.add(neighbor);
					toExplore.add(neighbor);
				} else {
					System.out.println("\tNeighbor already visited - " + neighbor);
				}
			}
			if (numNeighborsAtNextLevel == 0) {
				// There are no shortest paths that pass through current node
				System.out.println("Leaf node identified - " + currentNode);
				leafNodes.add(currentNode);
			}
		}
		System.out.println("Last node - " + currentNode);
		return currentNode;
	}

	private void updateDistanceAndWeightForRemainingNodes(SocialNetworkNode nodeI, SocialNetworkNode nodeJ) {
		// For each vertex j adjacent to i
		// if j has not been assigned
		// Dj = Di + 1
		// Wj = Wi
		// else
		// if Dj = Di + 1
		// Wj = Wj + Wi
		Integer newDistance;
		Integer newWeight;

		System.out.println("\tupdateDistanceAndWeight...");
		System.out.println("\tnodeI - " + nodeI);
		System.out.println("\tnodeJ - " + nodeJ);

		if (nodeJ.getDistance() == null) {
			newDistance = nodeI.getDistance() + 1;
			newWeight = nodeI.getWeight();

			nodeJ.setDistance(newDistance);
			nodeJ.setWeight(newWeight);

			System.out.println("\t\t\tCase (a) - distance not assigned yet");
			System.out.println("\t\t\tUpdated distance - " + newDistance);
			System.out.println("\t\t\tUpdated  weight - " + newWeight);
			System.out.println("\t\t\tnodeJ after updation - " + nodeJ);

		} else {
			if (nodeJ.getDistance() == nodeI.getDistance() + 1) {
				newWeight = nodeJ.getWeight() + nodeI.getWeight();
				nodeJ.setWeight(newWeight);

				System.out.println("\t\t\tCase (b) - distance assigned and dj = di+1 ");
				System.out.println("\t\t\tUpdated weight - " + newWeight);
				System.out.println("\t\t\tnodeJ after updation - " + nodeJ);
			}
			if (nodeJ.getDistance() < (nodeI.getDistance() + 1)) {
				System.out.println("\t\t\tCase (c) - distance assigned and dj < di + 1");
				System.out.println("\t\t\tSkipping");
			}
		}
	}

	private boolean moreNodesToProcess() {
		System.out.println("\nChecking if there are any nodes left to process");
		for (SocialNetworkNode node : graph.values()) {
			if (node.getDistance() != null) {
				for (SocialNetworkNode neighbor : node.getNeighbors()) {
					if (neighbor.getDistance() == null) {
						System.out.println(neighbor + " 's neighbor's distance yet to be updated - " + neighbor);
						return true;
					}
				}
			}
		}
		System.out.println("All nodes updated!");
		return false;
	}

	public void updateEdgeScore(SocialNetworkNode endNode, ArrayList<SocialNetworkNode> leafNodes) {
		System.out.println("Updating edge score");
		// STEP I
		updateEdgeScoreForLeafNodes(leafNodes);

		// STEP II
		updateEdgeScoreForRemainingEdges(endNode);
	}

	private void updateEdgeScoreForLeafNodes(ArrayList<SocialNetworkNode> leafNodes) {
		// For each edge E (t->i) such that t is a leaf vertex, assign a score -
		// Wi/Wt
		System.out.println("Updating edge score for leaf nodes");
		for (SocialNetworkNode leafNode : leafNodes) {
			for (SocialNetworkNode leafNodeNeighbor : leafNode.getNeighbors()) {
				Float score = new Float((float) leafNodeNeighbor.getWeight() / (float) leafNode.getWeight());
				updateEdgeScore(leafNodeNeighbor, leafNode, score);
			}
		}
	}

	private void updateEdgeScore(SocialNetworkNode firstEnd, SocialNetworkNode secondEnd, Float score) {
		System.out.println("\n\t\tUpdating new scores");
		SocialNetworkEdge edge = secondEnd.getEdgeCorrespondingToNeighbor(firstEnd);
		SocialNetworkEdge edgeFlipped = firstEnd.getEdgeCorrespondingToNeighbor(secondEnd);

		System.out.println("\t\tBefore updation - ");
		System.out.println("\t\t\t" + edge);
		System.out.println("\t\t\t" + edgeFlipped);

		edge.setBetweenness(score);
		edgeFlipped.setBetweenness(score);

		System.out.println("\t\tAfter updation - ");
		System.out.println("\t\t\t" + edge);
		System.out.println("\t\t\t" + edgeFlipped);
	}

	private void updateEdgeScoreForRemainingEdges(SocialNetworkNode endNode) {
		System.out.println("\nUpdating edge score for remaining nodes");
		// For each non-leaf edge starting from farthest edge, assign a score -
		// (1 + sum of scores of neighboring edges immediately below it) x (Wi/Wj)

		HashSet<SocialNetworkNode> visited = new HashSet<SocialNetworkNode>();
		Queue<SocialNetworkNode> toExplore = new LinkedList<SocialNetworkNode>();
		toExplore.add(endNode);

		while (!toExplore.isEmpty()) {
			System.out.println("\ntoExplore - " + toExplore);
			SocialNetworkNode currentNode = toExplore.remove();
			System.out.println("currentNode - " + currentNode);
			if (visited.contains(currentNode)) {
				System.out.println("Already visited. Skipping");
				continue;
			}
			visited.add(currentNode);
			System.out.println("Updated visited - " + visited);
			Set<SocialNetworkNode> neighborsOfCurrentNode = currentNode.getNeighbors();
			System.out.println("Neighbors to visit - " + neighborsOfCurrentNode);

			for (SocialNetworkNode neighbor : currentNode.getNeighbors()) {
				System.out.println("\n\tAt neighbor - " + neighbor);
				toExplore.add(neighbor);
				System.out.println("\tUpdated toExplore after adding neighbor - " + toExplore);
				SocialNetworkEdge neighborEdge = currentNode.getEdgeCorrespondingToNeighbor(neighbor);
				System.out.println("\tNeighbor edge - " + neighborEdge);

				if (neighborEdge.getBetweenness() != null) {
					System.out.println("\tScore already updated. Skipping! - ");
					continue;
				}

				// Update score
				boolean scoreUpdateSuccesful = calculateAndUpdateScoreForEdge(currentNode, neighbor, neighborEdge);
				if (!scoreUpdateSuccesful) {
					System.out.println(
							"\n\tCouldn't update the score for edge - " + neighborEdge + " from node " + currentNode);
					toExplore.add(currentNode);
					visited.remove(currentNode);
					System.out.println("\tRemoved current node from visited and added to toExplore for revisiting");
				}
			}
		}
	}

	private boolean calculateAndUpdateScoreForEdge(SocialNetworkNode currentNode, SocialNetworkNode neighbor,
			SocialNetworkEdge edge) {
		System.out.println("\n\t\tCalculating and updating score for - " + edge);
		System.out.println("\t\tcurrentNode - " + currentNode);
		System.out.println("\t\tneighbor - " + neighbor);
		Integer distanceOfCurrentNode = currentNode.getDistance();
		Integer distanceOfNeighbor = neighbor.getDistance();
		Float sumOfScoresOfBelowEdges = 0.0f;
		Float newScore = 0.0f;
		System.out.println("\t\tsumOfScoresOfBelowEdges - " + sumOfScoresOfBelowEdges);
		System.out.println("\t\tNeighbors of current node to visit - " + currentNode.getNeighbors());

		for (SocialNetworkNode neighborOfCurrentNode : currentNode.getNeighbors()) {
			System.out.println("\n\t\t\tNeighbor of current node - " + neighborOfCurrentNode);
			if (neighborOfCurrentNode.equals(neighbor)) {
				System.out.println("\t\t\tThis is the neighbor whose edge score we are calculating. Skipping");
				continue;
			}
			Integer distanceOfNeighborOfCurrentNode = neighborOfCurrentNode.getDistance();
			System.out.println("\t\t\tdistance of current node - " + distanceOfCurrentNode);
			System.out.println("\t\t\tdistance of neighbor - " + distanceOfNeighbor);
			System.out.println("\t\t\tdistance of neighbor of current node - " + distanceOfNeighborOfCurrentNode);

			if (distanceOfNeighborOfCurrentNode < distanceOfCurrentNode) {
				System.out.println("\t\t\tNeighbor is not below current node. Skipping!");
				continue;
			}
			if (distanceOfNeighborOfCurrentNode <= distanceOfNeighbor) {
				System.out.println("\t\t\tNeighbor of current node is parallel to neighbor. Can't contribute to score");
				continue;
			}
			SocialNetworkEdge neighborEdge = currentNode.getEdgeCorrespondingToNeighbor(neighborOfCurrentNode);
			System.out.println("\t\t\tneighborEdge - " + neighborEdge);

			if (neighborEdge.getBetweenness() == null) {
				System.out.println("\t\t\tneighborEdge doesn't have a score - " + neighborEdge);
				System.out.println("\n\t\tCouldn't update the score for edge - " + edge + " from node " + currentNode
						+ " as one of the neighbor edge doesn't have score yet");
				return false;
			}
			System.out.println("\t\t\tAdding neighborEdge's contribution to score");
			sumOfScoresOfBelowEdges += neighborEdge.getBetweenness();
			System.out.println("\t\t\tsumOfScoresOfBelowEdges - " + sumOfScoresOfBelowEdges);
		}
		if (sumOfScoresOfBelowEdges > 0.0f) {
			newScore = (1 + sumOfScoresOfBelowEdges) * ((float) neighbor.getWeight() / (float) currentNode.getWeight());
			System.out.println("\n\t\tnewScore - " + newScore);
			updateEdgeScore(neighbor, currentNode, newScore);
			return true;
		} else {
			return false;
		}
	}

	public HashMap<SocialNetworkEdge, Float> getEdgeToBetweennessMap() {
		HashMap<SocialNetworkEdge, Float> edgeToBetweennessMap = new HashMap<SocialNetworkEdge, Float>();
		for (SocialNetworkEdge edge : getEdges()) {
			edgeToBetweennessMap.put(edge, edge.getBetweenness());
		}
		return edgeToBetweennessMap;
	}

	public HashMap<SocialNetworkEdge, Float> computeBetweenness() {
		System.out.println("\nComputing betweenness");
		/*
		 * Initialize empty edgeToBetweennessMap For every pair of shortest path:
		 * Compute distance & weight Compute edge score Update edgeToBetweennessMap
		 * Return edgeToBetweennessMap
		 */
		HashMap<SocialNetworkEdge, Float> edgeToBetweennessMap = getEdgeToBetweennessMap();
		HashMap<SocialNetworkEdge, Float> edgeToBetweennessMapNew;
		Collection<SocialNetworkNode> nodes = getGraph().values();
		System.out.println("Total nodes - " + nodes.size());
		int count = 1;

		for (SocialNetworkNode startNode : nodes) {
			String status = "At node - " + count + "/" + nodes.size();
			System.out.println("\n" + status);
			System.out.println("---------------");

			System.out.println("Running the algorithm for startNode - " + startNode);
			System.out.println("\n" + status + " | Step I - Updating distance & weight");

			System.out.println("\n" + status + " | Resetting nodes & edges");
			System.out.println("Before - " + graph);
			resetNodesAndEdges(nodes);
			System.out.println("After - " + graph);

			// Compute distance & weight
			ArrayList<SocialNetworkNode> leafNodes = new ArrayList<SocialNetworkNode>();

			SocialNetworkNode lastNode = updateDistanceAndWeights(startNode, leafNodes);

			System.out.println("\nGraph after updating distances & weight - " + graph);

			// Compute edge score
			System.out.println("\n" + status + " | Step II - Updating edge scores");

			updateEdgeScore(lastNode, leafNodes);

			System.out.println("\n" + status + " | Step III - Updating betweenness");
			System.out.println("Before - " + edgeToBetweennessMap);

			// Update edgeToBetweennessMap
			edgeToBetweennessMapNew = getEdgeToBetweennessMap();

			updateEdgeToBetweennessMap(edgeToBetweennessMap, edgeToBetweennessMapNew);

			System.out.println("Content to update - " + edgeToBetweennessMapNew);
			System.out.println("After - " + edgeToBetweennessMap);
			count += 1;
			System.out.println();
		}

		reduceBetweennessByHalf(edgeToBetweennessMap);
		System.out.println("\nFinished computing betweenness");
		return edgeToBetweennessMap;
	}

	private void reduceBetweennessByHalf(HashMap<SocialNetworkEdge, Float> edgeToBetweennessMap) {
		for (SocialNetworkEdge edge : edgeToBetweennessMap.keySet()) {
			float newBetweenness = edgeToBetweennessMap.get(edge) / 2.0f;
			DecimalFormat newFormat = new DecimalFormat("#.##");
			newBetweenness = Float.valueOf(newFormat.format(newBetweenness));
			edgeToBetweennessMap.put(edge, newBetweenness);
		}
	}

	private void resetNodesAndEdges(Collection<SocialNetworkNode> nodes) {
		for (SocialNetworkNode node : nodes) {
			node.resetNode();
		}
	}

	void updateEdgeToBetweennessMap(HashMap<SocialNetworkEdge, Float> oldMap,
			HashMap<SocialNetworkEdge, Float> newMap) {
		for (Entry<SocialNetworkEdge, Float> entry : oldMap.entrySet()) {
			SocialNetworkEdge edgeInOldMap = entry.getKey();
			Float oldValue = entry.getValue();
			Float newValue = newMap.get(edgeInOldMap);
			if (newValue == null) {
				System.out.println("New score not available in newMap for edge - " + edgeInOldMap);
				continue;
			}
			oldValue = oldValue == null ? 0.0f : oldValue;
			oldMap.put(edgeInOldMap, oldValue + newValue);
		}
	}

	public List<SocialNetworkGraph> getConnectedComponents() {
		List<SocialNetworkGraph> listOfCC = new ArrayList<SocialNetworkGraph>();

		// Initialize
		List<Stack<SocialNetworkNode>> listOfStackOfNodesInSCCs = new ArrayList<Stack<SocialNetworkNode>>();

		// Step 1 - Run DFS on G and store the order of traversal
		Stack<SocialNetworkNode> initialDFS = DFS(listOfStackOfNodesInSCCs);
		System.out.println("Initial DFS - " + initialDFS);

		System.out.println("List of list of nodes in SCC - " + listOfStackOfNodesInSCCs);

		// Post processing
		listOfCC = constructCCsfromNodes(listOfStackOfNodesInSCCs);

		System.out.println("List of SCCs - " + listOfCC);

		return listOfCC;
	}

	private List<SocialNetworkGraph> constructCCsfromNodes(List<Stack<SocialNetworkNode>> listOfStackOfNodesInCCs) {
		List<SocialNetworkGraph> listOfCCs = new ArrayList<SocialNetworkGraph>();
		for (Stack<SocialNetworkNode> stackOfNodes : listOfStackOfNodesInCCs) {
			listOfCCs.add(constructGraphFromStackOfNodes(stackOfNodes));
		}
		return listOfCCs;
	}

	private SocialNetworkGraph constructGraphFromStackOfNodes(Stack<SocialNetworkNode> stackOfNodes) {
		SocialNetworkGraph newGraph = new SocialNetworkGraph();
		while (!stackOfNodes.isEmpty()) {
			SocialNetworkNode node = stackOfNodes.pop();
			newGraph.addVertex(node);
		}
		return newGraph;
	}

	public Stack<SocialNetworkNode> DFS(List<Stack<SocialNetworkNode>> listOfListOfNodesInSCCs) {
		// System.out.println("\n\nRunning DFS on - " + graph);
		// System.out.println(" Vertices to visit - " + vertices);

		HashSet<SocialNetworkNode> visited = new HashSet<SocialNetworkNode>();
		Stack<SocialNetworkNode> finished = new Stack<SocialNetworkNode>();

		for (SocialNetworkNode vertex : graph.values()) {
			if (!visited.contains(vertex)) {
				Stack<SocialNetworkNode> exploredVerticesInThisRun = new Stack<SocialNetworkNode>();
				DFSVisit(vertex, visited, finished, exploredVerticesInThisRun);
				listOfListOfNodesInSCCs.add(exploredVerticesInThisRun);

				// System.out.println("Finished - " + finished);
				// System.out.println("Explored vertices - " + exploredVerticesInThisRun);
			}

		}

		return finished;
	}

	public void DFSVisit(SocialNetworkNode vertex, HashSet<SocialNetworkNode> visited,
			Stack<SocialNetworkNode> finished, Stack<SocialNetworkNode> exploredVerticesInThisRun) {
		visited.add(vertex);
		Collection<SocialNetworkNode> neighbors = vertex.getNeighbors();

		// System.out.println("\nVisiting - " + vertex);
		// System.out.println("Neighbors of " + vertex + " - " + neighbors);
		// System.out.println("Visited - " + visited);

		for (SocialNetworkNode neighbor : neighbors) {
			if (!visited.contains(neighbor)) {
				visited.add(neighbor);
				DFSVisit(neighbor, visited, finished, exploredVerticesInThisRun);
			}
		}
		finished.add(vertex);
		exploredVerticesInThisRun.push(vertex);
		// System.out.println("Done with " + vertex + " - no more unvisited neighbors");
		// System.out.println("Adding to finished and exploredVerticesInThisRun- " +
		// vertex);
		// System.out.println("Finished - " + finished);
		// System.out.println("Explored vertices in this run - " +
		// exploredVerticesInThisRun);
	}

	public List<SocialNetworkGraph> getCommunities(Integer iterations) {
		System.out.println("\nCOMMUNITY DETECTION: Detecting communities with iterations - " + iterations);
		// Algo
		// Repeat for depthConstant times:
		// STEP I - Compute betweenness of all edges
		// STEP II - Remove egde(s) with highest betweenness
		// STEP III - Return resulting subgraphs
		List<SocialNetworkGraph> communities = new ArrayList<SocialNetworkGraph>();

		while (iterations > 0) {
			System.out.println("\nCOMMUNITY DETECTION: Iteration - " + iterations);
			// STEP I
			System.out.println("\nCOMMUNITY DETECTION: STEP I - Computing betweenness");
			HashMap<SocialNetworkEdge, Float> edgeToBetweennessMap = computeBetweenness();

			// STEP II
			System.out.println("\nCOMMUNITY DETECTION: STEP II - Removing edges with max betweenness");
			removeEdgesWithMaxBetweenness(edgeToBetweennessMap);

			iterations--;
		}
		System.out.println("\nCOMMUNITY DETECTION: STEP III - Retrieving subgraphs");
		communities = getConnectedComponents();
		System.out.println("\nCOMMUNITY DETECTION: Detected communities - " + communities.size());
		System.out.println(communities);
		System.out.println("\nCOMMUNITY DETECTION: FINISHED");
		return communities;
	}

	private void removeEdgesWithMaxBetweenness(HashMap<SocialNetworkEdge, Float> edgeToBetweennessMap) {
		System.out.println("Removing edges with max betweenness");
		System.out.println("edgeToBetweennessMap - " + edgeToBetweennessMap);
		List<SocialNetworkEdge> edgesToRemove = new ArrayList<SocialNetworkEdge>();

		Float maxBetweenness = Collections.max(edgeToBetweennessMap.values());
		System.out.println("Max betweeness - " + maxBetweenness);
		for (Entry<SocialNetworkEdge, Float> entry : edgeToBetweennessMap.entrySet()) {
			SocialNetworkEdge edge = entry.getKey();
			Float betweenness = entry.getValue();
			if (betweenness.equals(maxBetweenness))
				edgesToRemove.add(edge);
		}
		System.out.println("Edges to remove - " + edgesToRemove);
		removeEdges(edgesToRemove);
	}

	private void removeEdges(List<SocialNetworkEdge> edgesToRemove) {
		for (SocialNetworkEdge edge : edgesToRemove) {
			removeEdge(edge);
		}
	}
}
