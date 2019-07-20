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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SocialNetworkGraph implements Graph {
	private static final Logger logger = LogManager.getLogger(SocialNetworkGraph.class);
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
			return;
		}
		if (secondEnd == null) {
			logger.debug("First end is null. Can't add edge!");
			return;
		}
		SocialNetworkEdge edge = new SocialNetworkEdge(firstEnd, secondEnd);

		if (firstEnd.getEdges().contains(edge) && secondEnd.getEdges().contains(edge)) {
			logger.debug("Edge already exists - {}", edge);
			return;
		}
		firstEnd.addNeighbor(secondEnd);
		secondEnd.addNeighbor(firstEnd);
		numEdges++;
	}

	private void addEdge(SocialNetworkNode firstEnd, SocialNetworkNode secondEnd, float betweenness) {
		if (firstEnd == null) {
			logger.debug("First end is doesn't exist!");
			return;
		}
		if (secondEnd == null) {
			logger.debug("First end is null. Can't add edge!");
			return;
		}
		SocialNetworkEdge edge = new SocialNetworkEdge(firstEnd, secondEnd, betweenness);

		if (firstEnd.getEdges().contains(edge) && secondEnd.getEdges().contains(edge)) {
			logger.debug("Edge already exists - {}", edge);
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
			logger.debug("Either start node or goal node is not present. Aborting search!");
			return new LinkedList<SocialNetworkNode>();
		}
		// Find path using breadth first search
		path = findPathUsingBFS(startNode, endNode);

		if (path.isEmpty()) {
			logger.debug("Sorry, Goal not found!!");
		}
		return path;
	}

	protected List<SocialNetworkNode> findPathUsingBFS(SocialNetworkNode startNode, SocialNetworkNode endNode) {
		Set<SocialNetworkNode> visited = new HashSet<SocialNetworkNode>();
		Queue<SocialNetworkNode> toExplore = new LinkedList<SocialNetworkNode>();
		Map<SocialNetworkNode, SocialNetworkNode> parentMap = new HashMap<SocialNetworkNode, SocialNetworkNode>();
		logger.debug("Running BFS......");
		logger.debug("Start - {}", startNode);
		logger.debug("Goal - {}", endNode);

		// do bfs
		boolean found = false;
		toExplore.add(startNode);

		while (!toExplore.isEmpty()) {
			SocialNetworkNode currentNode = toExplore.remove();
			logger.debug("toExplore - {}", toExplore);
			logger.debug("currentNode - {}", currentNode);

			if (currentNode.equals(endNode)) {
				logger.debug("MATCH FOUND!");
				found = true;
				break;
			}

			Set<SocialNetworkNode> neighbors = currentNode.getNeighbors();
			logger.debug("found - {}", found);
			logger.debug("visited - {}", visited);
			logger.debug("visiting neighbors - {}", neighbors);

			for (SocialNetworkNode neighbor : neighbors) {
				logger.debug("checking neighbor - {}", neighbor);
				if (!visited.contains(neighbor)) {
					parentMap.put(neighbor, currentNode);
					visited.add(neighbor);
					toExplore.add(neighbor);
				} else {
					logger.debug("neighbor already visited - {}", neighbor);
				}
			}
			logger.debug("toExplore - {}", toExplore);
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
		logger.debug("Removing edge - {}", edge);

		Integer firstVertex = edge.getFirstEnd().getItem();
		Integer secondVertex = edge.getSecondEnd().getItem();

		removeEdge(firstVertex, secondVertex);
	}

	public void removeEdge(int from, int to) {
		logger.debug("Removing edge between {} and 0{}", from, to);

		SocialNetworkNode firstEnd = graph.get(from);
		SocialNetworkNode secondEnd = graph.get(to);

		if (firstEnd == null || secondEnd == null) {
			logger.debug("Invalid edge. Either first end or second end or both ends don't exist");
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
		String s = "\n";
//		s += " #Vertices - " + graph.size();
//		s += ", #Edges - " + getEdges().size();
		// s += "\n Edges - " + getStringifiedEdgesList();
//		s += "\n Graph - " + getStringifiedGraph();
		s += graph.values();
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
			logger.debug("Node doesnt exist for {}", num);
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
		logger.debug("Running updateDistanceAndWeightsViaBFS......\n");

		Map<SocialNetworkNode, SocialNetworkNode> parentMap = new HashMap<SocialNetworkNode, SocialNetworkNode>();
		SocialNetworkNode lastNode;

		// Step I
		logger.debug("Updating starting node - {}", nodeS);
		updateStartingNode(nodeS);

		// Step II
		logger.debug("Updating nodes adjacent to starting node");
		updateNodesAdjacentToStartingNode(nodeS, parentMap);

		// Step III
		logger.debug("Updating remaining nodes");
		lastNode = updateRemainingNodes(nodeS, parentMap, leafNodes);

		logger.debug("Last node - {}", lastNode);
		logger.debug("Parent map - {}", parentMap);
		logger.debug("Leaf nodes - {}", leafNodes);

		return lastNode;
	}

	private void updateStartingNode(SocialNetworkNode nodeS) {
		// Step I
		// Initial vertex s is given a distance
		// Ds = 0
		// Ws = 1

		logger.debug("Beginning with start node, nodeS - {}", nodeS);

		Integer newDistance = 0;
		Integer newWeight = 1;
		nodeS.setDistance(0);
		nodeS.setWeight(1);

		logger.debug("Updated distance - {}", newDistance);
		logger.debug("Updated  weight - {}", newWeight);
		logger.debug("Node after updation - {}", nodeS);

	}

	private void updateNodesAdjacentToStartingNode(SocialNetworkNode nodeS,
			Map<SocialNetworkNode, SocialNetworkNode> parentMap) {
		// Every vertex adjacent to starting node is given
		// Di = Ds + 1 = 1
		// Wi = Ws = 1

		logger.debug("nodeS neighbors - {}", nodeS.getNeighbors());

		Integer newDistance;
		Integer newWeight;

		for (SocialNetworkNode nodeI : nodeS.getNeighbors()) {
			logger.debug("nodeI - {}", nodeI);
			logger.debug("Node is adjacent to start node - {}", nodeI);

			parentMap.put(nodeI, nodeS);

			newDistance = nodeS.getDistance() + 1;
			newWeight = nodeS.getWeight();
			nodeI.setDistance(newDistance);
			nodeI.setWeight(newWeight);

			logger.debug("Updated distance - {}", newDistance);
			logger.debug("Updated  weight - {}", newWeight);
			logger.debug("Node after updation - {}", nodeI);
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
			logger.debug("No neighbors to explore");
			return null;
		}
		toExplore.addAll(neighorsOfNodeS);
		visited.add(nodeS);
		visited.addAll(neighorsOfNodeS);

		logger.debug("Starting BFS exploration");
		SocialNetworkNode currentNode = null;
		while (!toExplore.isEmpty()) {
			logger.debug("toExplore - {}", toExplore);
			currentNode = toExplore.remove();
			logger.debug("currentNode - {}", currentNode);

			Set<SocialNetworkNode> neighbors = currentNode.getNeighbors();
			logger.debug("Visited - {}", visited);
			logger.debug("Visiting neighbors - {}", neighbors);

			Integer numNeighborsAtNextLevel = 0;
			for (SocialNetworkNode neighbor : neighbors) {
				logger.debug("At neighbor - {}", neighbor);

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
					logger.debug("Neighbor already visited - {}", neighbor);
				}
			}
			if (numNeighborsAtNextLevel == 0) {
				// There are no shortest paths that pass through current node
				logger.debug("Leaf node identified - {}", currentNode);
				leafNodes.add(currentNode);
			}
		}
		logger.debug("Last node - {}", currentNode);
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

		logger.debug("updateDistanceAndWeight");
		logger.debug("nodeI - {}", nodeI);
		logger.debug("nodeJ - {}", nodeJ);

		if (nodeJ.getDistance() == null) {
			newDistance = nodeI.getDistance() + 1;
			newWeight = nodeI.getWeight();

			nodeJ.setDistance(newDistance);
			nodeJ.setWeight(newWeight);

			logger.debug("Case (a) - distance not assigned yet");
			logger.debug("Updated distance - {}", newDistance);
			logger.debug("Updated  weight - {}", newWeight);
			logger.debug("nodeJ after updation - {}", nodeJ);

		} else {
			if (nodeJ.getDistance() == nodeI.getDistance() + 1) {
				newWeight = nodeJ.getWeight() + nodeI.getWeight();
				nodeJ.setWeight(newWeight);

				logger.debug("Case (b) - distance assigned and dj = di+1 ");
				logger.debug("Updated weight - {}", newWeight);
				logger.debug("nodeJ after updation - {}", nodeJ);
			}
			if (nodeJ.getDistance() < (nodeI.getDistance() + 1)) {
				logger.debug("Case (c) - distance assigned and dj < di + 1");
				logger.debug("Skipping");
			}
		}
	}

	private boolean moreNodesToProcess() {
		logger.debug("Checking if there are any nodes left to process");
		for (SocialNetworkNode node : graph.values()) {
			if (node.getDistance() != null) {
				for (SocialNetworkNode neighbor : node.getNeighbors()) {
					if (neighbor.getDistance() == null) {
						logger.debug("{}'s neighbor's distance yet to be updated - ", neighbor);
						return true;
					}
				}
			}
		}
		logger.debug("All nodes updated!");
		return false;
	}

	public void updateEdgeScore(SocialNetworkNode endNode, ArrayList<SocialNetworkNode> leafNodes) {
		logger.debug("Updating edge score");
		// STEP I
		updateEdgeScoreForLeafNodes(leafNodes);

		// STEP II
		updateEdgeScoreForRemainingEdges(endNode);
	}

	private void updateEdgeScoreForLeafNodes(ArrayList<SocialNetworkNode> leafNodes) {
		// For each edge E (t->i) such that t is a leaf vertex, assign a score -
		// Wi/Wt
		logger.debug("Updating edge score for leaf nodes");
		for (SocialNetworkNode leafNode : leafNodes) {
			for (SocialNetworkNode leafNodeNeighbor : leafNode.getNeighbors()) {
				Float score = new Float((float) leafNodeNeighbor.getWeight() / (float) leafNode.getWeight());
				updateEdgeScore(leafNodeNeighbor, leafNode, score);
			}
		}
	}

	private void updateEdgeScore(SocialNetworkNode firstEnd, SocialNetworkNode secondEnd, Float score) {
		logger.debug("Updating new scores");
		SocialNetworkEdge edge = secondEnd.getEdgeCorrespondingToNeighbor(firstEnd);
		SocialNetworkEdge edgeFlipped = firstEnd.getEdgeCorrespondingToNeighbor(secondEnd);

		logger.debug("Before updation - ");
		logger.debug(edge);
		logger.debug(edgeFlipped);

		edge.setBetweenness(score);
		edgeFlipped.setBetweenness(score);

		logger.debug("After updation - ");
		logger.debug(edge);
		logger.debug(edgeFlipped);
	}

	private void updateEdgeScoreForRemainingEdges(SocialNetworkNode endNode) {
		logger.debug("Updating edge score for remaining nodes");
		// For each non-leaf edge starting from farthest edge, assign a score -
		// (1 + sum of scores of neighboring edges immediately below it) x (Wi/Wj)

		HashSet<SocialNetworkNode> visited = new HashSet<SocialNetworkNode>();
		Queue<SocialNetworkNode> toExplore = new LinkedList<SocialNetworkNode>();
		toExplore.add(endNode);

		while (!toExplore.isEmpty()) {
			logger.debug("toExplore - {}", toExplore);
			SocialNetworkNode currentNode = toExplore.remove();
			logger.debug("currentNode - {}", currentNode);
			if (visited.contains(currentNode)) {
				logger.debug("Already visited. Skipping");
				continue;
			}
			visited.add(currentNode);
			logger.debug("Updated visited - {}", visited);
			Set<SocialNetworkNode> neighborsOfCurrentNode = currentNode.getNeighbors();
			logger.debug("Neighbors to visit - {}", neighborsOfCurrentNode);

			for (SocialNetworkNode neighbor : currentNode.getNeighbors()) {
				logger.debug("At neighbor - {}", neighbor);
				toExplore.add(neighbor);
				logger.debug("Updated toExplore after adding neighbor - {}", toExplore);
				SocialNetworkEdge neighborEdge = currentNode.getEdgeCorrespondingToNeighbor(neighbor);
				logger.debug("Neighbor edge - {}", neighborEdge);

				if (neighborEdge.getBetweenness() != null) {
					logger.debug("Score already updated. Skipping! - {}", neighborEdge);
					continue;
				}

				// Update score
				boolean scoreUpdateSuccesful = calculateAndUpdateScoreForEdge(currentNode, neighbor, neighborEdge);
				if (!scoreUpdateSuccesful) {
					logger.debug("Couldn't update the score for edge - {} from node {}", neighborEdge, currentNode);
					toExplore.add(currentNode);
					visited.remove(currentNode);
					logger.debug("Removed current node from visited and added to toExplore for revisiting");
				}
			}
		}
	}

	private boolean calculateAndUpdateScoreForEdge(SocialNetworkNode currentNode, SocialNetworkNode neighbor,
			SocialNetworkEdge edge) {
		logger.debug("Calculating and updating score for - {}", edge);
		logger.debug("currentNode - {}", currentNode);
		logger.debug("neighbor - {}", neighbor);
		Integer distanceOfCurrentNode = currentNode.getDistance();
		Integer distanceOfNeighbor = neighbor.getDistance();
		Float sumOfScoresOfBelowEdges = 0.0f;
		Float newScore = 0.0f;
		logger.debug("sumOfScoresOfBelowEdges - {}", sumOfScoresOfBelowEdges);
		logger.debug("Neighbors of current node to visit - {}", currentNode.getNeighbors());

		for (SocialNetworkNode neighborOfCurrentNode : currentNode.getNeighbors()) {
			logger.debug("Neighbor of current node - {}", neighborOfCurrentNode);
			if (neighborOfCurrentNode.equals(neighbor)) {
				logger.debug("This is the neighbor whose edge score we are calculating. Skipping");
				continue;
			}
			Integer distanceOfNeighborOfCurrentNode = neighborOfCurrentNode.getDistance();
			logger.debug("distance of current node - {}", distanceOfCurrentNode);
			logger.debug("distance of neighbor - {}", distanceOfNeighbor);
			logger.debug("distance of neighbor of current node - {}", distanceOfNeighborOfCurrentNode);

			if (distanceOfNeighborOfCurrentNode < distanceOfCurrentNode) {
				logger.debug("Neighbor is not below current node. Skipping!");
				continue;
			}
			if (distanceOfNeighborOfCurrentNode <= distanceOfNeighbor) {
				logger.debug("Neighbor of current node is parallel to neighbor. Can't contribute to score");
				continue;
			}
			SocialNetworkEdge neighborEdge = currentNode.getEdgeCorrespondingToNeighbor(neighborOfCurrentNode);
			logger.debug("neighborEdge - {}", neighborEdge);

			if (neighborEdge.getBetweenness() == null) {
				logger.debug("neighborEdge doesn't have a score - {}", neighborEdge);
				logger.debug("Couldn't update the score for edge - {} from node {}"
						+ " as one of the neighbor edge doesn't have score yet", edge, currentNode);
				return false;
			}
			logger.debug("Adding neighborEdge's contribution to score");
			sumOfScoresOfBelowEdges += neighborEdge.getBetweenness();
			logger.debug("sumOfScoresOfBelowEdges - {}", sumOfScoresOfBelowEdges);
		}
		if (sumOfScoresOfBelowEdges > 0.0f) {
			newScore = (1 + sumOfScoresOfBelowEdges) * ((float) neighbor.getWeight() / (float) currentNode.getWeight());
			logger.debug("newScore - {}", newScore);
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
		logger.debug("Computing betweenness");
		/*
		 * Initialize empty edgeToBetweennessMap For every pair of shortest path:
		 * Compute distance & weight Compute edge score Update edgeToBetweennessMap
		 * Return edgeToBetweennessMap
		 */
		HashMap<SocialNetworkEdge, Float> edgeToBetweennessMap = getEdgeToBetweennessMap();
		HashMap<SocialNetworkEdge, Float> edgeToBetweennessMapNew;
		Collection<SocialNetworkNode> nodes = getGraph().values();
		logger.debug("Total nodes - {}", nodes.size());
		int count = 1;

		for (SocialNetworkNode startNode : nodes) {
			logger.debug("status - At node {}/{}", count, nodes.size());
			logger.debug("Running the algorithm for startNode - {}", startNode);
			logger.debug("At node - {}/{} | Step I - Updating distance & weight", count, nodes.size());

			logger.debug("At node - {}/{} | Resetting nodes & edges", count, nodes.size());
			logger.debug("Before - {}", graph);
			resetNodesAndEdges(nodes);
			logger.debug("After - {}", graph);

			// Compute distance & weight
			ArrayList<SocialNetworkNode> leafNodes = new ArrayList<SocialNetworkNode>();

			SocialNetworkNode lastNode = updateDistanceAndWeights(startNode, leafNodes);

			logger.debug("Graph after updating distances & weight - {}", graph);

			// Compute edge score
			logger.debug("At node - {}/{} | Step II - Updating edge scores", count, nodes.size());

			updateEdgeScore(lastNode, leafNodes);

			logger.debug("At node - {}/{}| Step III - Updating betweenness", count, nodes.size());
			logger.debug("Before - {}", edgeToBetweennessMap);

			// Update edgeToBetweennessMap
			edgeToBetweennessMapNew = getEdgeToBetweennessMap();

			updateEdgeToBetweennessMap(edgeToBetweennessMap, edgeToBetweennessMapNew);

			logger.debug("Content to update - {}", edgeToBetweennessMapNew);
			logger.debug("After - {}", edgeToBetweennessMap);
			count += 1;
		}

		reduceBetweennessByHalf(edgeToBetweennessMap);
		logger.debug("Finished computing betweenness");
		return edgeToBetweennessMap;
	}

	private void reduceBetweennessByHalf(Map<SocialNetworkEdge, Float> edgeToBetweennessMap) {
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
				logger.debug("New score not available in newMap for edge - {}", edgeInOldMap);
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
		logger.debug("Initial DFS - {}", initialDFS);

		logger.debug("List of list of nodes in SCC - {}", listOfStackOfNodesInSCCs);

		// Post processing
		listOfCC = constructCCsfromNodes(listOfStackOfNodesInSCCs);

		logger.debug("List of SCCs - {}", listOfCC);

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
		logger.debug("Running DFS on - {}", graph);

		HashSet<SocialNetworkNode> visited = new HashSet<SocialNetworkNode>();
		Stack<SocialNetworkNode> finished = new Stack<SocialNetworkNode>();

		for (SocialNetworkNode vertex : graph.values()) {
			if (!visited.contains(vertex)) {
				Stack<SocialNetworkNode> exploredVerticesInThisRun = new Stack<SocialNetworkNode>();
				DFSVisit(vertex, visited, finished, exploredVerticesInThisRun);
				listOfListOfNodesInSCCs.add(exploredVerticesInThisRun);

				logger.debug("Finished - {}", finished);
				logger.debug("Explored vertices - {}", exploredVerticesInThisRun);
			}

		}

		return finished;
	}

	public void DFSVisit(SocialNetworkNode vertex, HashSet<SocialNetworkNode> visited,
			Stack<SocialNetworkNode> finished, Stack<SocialNetworkNode> exploredVerticesInThisRun) {
		visited.add(vertex);
		Collection<SocialNetworkNode> neighbors = vertex.getNeighbors();

		logger.debug("Visiting - {}", vertex);
		logger.debug("Neighbors of {} - {}", vertex, neighbors);
		logger.debug("Visited - {}", visited);

		for (SocialNetworkNode neighbor : neighbors) {
			if (!visited.contains(neighbor)) {
				visited.add(neighbor);
				DFSVisit(neighbor, visited, finished, exploredVerticesInThisRun);
			}
		}
		finished.add(vertex);
		exploredVerticesInThisRun.push(vertex);
		logger.debug("Done with {} - no more unvisited neighbors", vertex);
		logger.debug("Adding to finished and exploredVerticesInThisRun - {}", vertex);
		logger.debug("Finished - {}", finished);
		logger.debug("Explored vertices in this run - {}", exploredVerticesInThisRun);
	}

	public List<SocialNetworkGraph> getCommunities(Integer iterations) {
		logger.info("COMMUNITY DETECTION: Detecting communities with iterations - {}", iterations);
		// Algo
		// Repeat for depthConstant times:
		// STEP I - Compute betweenness of all edges
		// STEP II - Remove egde(s) with highest betweenness
		// STEP III - Return resulting subgraphs
		List<SocialNetworkGraph> communities = new ArrayList<SocialNetworkGraph>();
		long startTime = 0;
		long computeBetweennessTime = 0;
		long removeEdgeTime = 0;
		long retrievingSubgraphTime = 0;

		while (iterations > 0) {
			logger.info("COMMUNITY DETECTION: Iteration - {}", iterations);
			// STEP I
			logger.info("COMMUNITY DETECTION: STEP I - Computing betweenness");
			startTime = System.currentTimeMillis();
			HashMap<SocialNetworkEdge, Float> edgeToBetweennessMap = computeBetweenness();
			computeBetweennessTime = System.currentTimeMillis() - startTime;
			logger.info("Took {}ms", computeBetweennessTime);
			// STEP II
			startTime = System.currentTimeMillis();
			logger.info("COMMUNITY DETECTION: STEP II - Removing edges with max betweenness");
			removeEdgesWithMaxBetweenness(edgeToBetweennessMap);
			removeEdgeTime = System.currentTimeMillis() - startTime;
			logger.info("Took {}ms", removeEdgeTime);
			iterations--;
		}
		logger.info("COMMUNITY DETECTION: STEP III - Retrieving subgraphs");
		startTime = System.currentTimeMillis();
		communities = getConnectedComponents();
		retrievingSubgraphTime = System.currentTimeMillis() - startTime;
		logger.info("Took {}ms", retrievingSubgraphTime);

		logger.info("COMMUNITY DETECTION: Detected communities - {}", communities.size());
		logger.info(communities);

		logger.info("COMMUNITY DETECTION: FINISHED");
		logger.info("Total time - {}", (computeBetweennessTime + removeEdgeTime + retrievingSubgraphTime));
		logger.info("Number of edges - {}", getEdges().size());
		logger.info("Number of vertices - {}", graph.size());
		return communities;
	}

    public List<SocialNetworkGraph> getCommunitiesUsingBrandes(Integer iterations) {
        logger.debug("COMMUNITY DETECTION: Detecting communities with iterations - {}", iterations);
        // Algo
        // Repeat for iterations times:
        // STEP I - Compute betweenness of all edges
        // STEP II - Remove edge(s) with highest betweenness
        // STEP III - Return resulting subgraphs
        List<SocialNetworkGraph> communities = new ArrayList<SocialNetworkGraph>();
        long startTime = 0;
        long computeBetweennessTime = 0;
        long removeEdgeTime = 0;
        long retrievingSubgraphTime = 0;

        while (iterations > 0) {
            logger.debug("COMMUNITY DETECTION: Iteration - {}", iterations);

            // STEP I
            startTime = System.currentTimeMillis();
            Map<SocialNetworkEdge, Float> edgeToBetweennessMap = computeBetweennessUsingBrandes();
            computeBetweennessTime = System.currentTimeMillis() - startTime;
            logger.debug("COMMUNITY DETECTION: STEP I - Computing betweenness - Took {}ms", computeBetweennessTime);

            // STEP II
            startTime = System.currentTimeMillis();
            removeEdgesWithMaxBetweenness(edgeToBetweennessMap);
            removeEdgeTime = System.currentTimeMillis() - startTime;
            logger.debug("COMMUNITY DETECTION: STEP II - Removing edges with max betweenness - Took {}ms", removeEdgeTime);

            iterations--;
        }

        // STEP III
        startTime = System.currentTimeMillis();
        communities = getConnectedComponents();
        retrievingSubgraphTime = System.currentTimeMillis() - startTime;
        logger.debug("COMMUNITY DETECTION: STEP III - Retrieving subgraphs - Took {}ms", retrievingSubgraphTime);

        logger.info("COMMUNITY DETECTION: Detected communities - {} - {}", communities.size(), communities);

        logger.debug("COMMUNITY DETECTION: FINISHED");
        logger.debug("Total time - {}", (computeBetweennessTime + removeEdgeTime + retrievingSubgraphTime));
        logger.debug("Number of edges - {}", getEdges().size());
        logger.debug("Number of vertices - {}", graph.size());
        return communities;
    }

	public List<SocialNetworkGraph> getAtLeastNCommunitiesUsingBrandes(Integer desiredCommunities) {
		logger.debug("COMMUNITY DETECTION: Detecting {} desiredCommunities", desiredCommunities);
		// Algo
		// Repeat for iterations times:
		// STEP I - Compute betweenness of all edges
		// STEP II - Remove edge(s) with highest betweenness
		// STEP III - Return resulting subgraphs
		List<SocialNetworkGraph> communities = new ArrayList<SocialNetworkGraph>();
		long startTime = 0;
		long computeBetweennessTime = 0;
		long removeEdgeTime = 0;
		long retrievingSubgraphTime = 0;
		int iteration = 1;
		while (communities.size() < desiredCommunities) {
			logger.debug("COMMUNITY DETECTION: Iteration - {}", iteration);

			// STEP I
			startTime = System.currentTimeMillis();
			Map<SocialNetworkEdge, Float> edgeToBetweennessMap = computeBetweennessUsingBrandes();
			computeBetweennessTime = System.currentTimeMillis() - startTime;
			logger.debug("COMMUNITY DETECTION: STEP I - Computing betweenness - Took {}ms", computeBetweennessTime);

			// STEP II
			startTime = System.currentTimeMillis();
			removeEdgesWithMaxBetweenness(edgeToBetweennessMap);
			removeEdgeTime = System.currentTimeMillis() - startTime;
			logger.debug("COMMUNITY DETECTION: STEP II - Removing edges with max betweenness - Took {}ms", removeEdgeTime);

			// STEP III
			startTime = System.currentTimeMillis();
			communities = getConnectedComponents();
			retrievingSubgraphTime = System.currentTimeMillis() - startTime;
			logger.debug("COMMUNITY DETECTION: STEP III - Retrieving subgraphs - Took {}ms", retrievingSubgraphTime);

			logger.info("COMMUNITY DETECTION: Detected communities - {} in {} iterations - {}", communities.size(),
					iteration, communities);

			iteration++;
		}

		logger.debug("COMMUNITY DETECTION: FINISHED");
		logger.debug("Total time - {}", (computeBetweennessTime + removeEdgeTime + retrievingSubgraphTime));
		logger.debug("Number of edges - {}", getEdges().size());
		logger.debug("Number of vertices - {}", graph.size());
		return communities;
	}

	private void removeEdgesWithMaxBetweenness(Map<SocialNetworkEdge, Float> edgeToBetweennessMap) {
		logger.debug("Removing edges with max betweenness");
		logger.debug("edgeToBetweennessMap - {}", edgeToBetweennessMap);
		List<SocialNetworkEdge> edgesToRemove = new ArrayList<SocialNetworkEdge>();

		Float maxBetweenness = Collections.max(edgeToBetweennessMap.values());
		logger.debug("Max betweeness - {}", maxBetweenness);
		for (Entry<SocialNetworkEdge, Float> entry : edgeToBetweennessMap.entrySet()) {
			SocialNetworkEdge edge = entry.getKey();
			Float betweenness = entry.getValue();
			if (betweenness.equals(maxBetweenness))
				edgesToRemove.add(edge);
		}
		logger.debug("Edges to remove - {}", edgesToRemove);
		removeEdges(edgesToRemove);
	}

	private void removeEdges(List<SocialNetworkEdge> edgesToRemove) {
		for (SocialNetworkEdge edge : edgesToRemove) {
			removeEdge(edge);
		}
	}

    public Map<SocialNetworkEdge, Float> computeBetweennessUsingBrandes() {
        // nodes to traverse
        Queue<SocialNetworkNode> queue = new LinkedList<>();

        // nodes to traverse in accumulation phase
        Stack<SocialNetworkNode> stack = new Stack<>();

        // distance from source
        Map<SocialNetworkNode, Integer> distance = new HashMap<>();

        // list of predecessors on shortest paths from source
        Map<SocialNetworkNode, Set<SocialNetworkNode>> predecessor = new HashMap<>();

        // number of shortest paths from source to v ∈ V
        Map<SocialNetworkNode, Integer> sigma = new HashMap<>();

        // dependency of source on v ∈ V
        Map<SocialNetworkNode, Float> delta = new HashMap<>();

        // node betweenness
        Map<SocialNetworkNode, Float> nodeBetweenness = new HashMap<>();
        for(SocialNetworkNode node: getGraph().values()){
            nodeBetweenness.put(node, 0.0f);
        }

        // edge betweenness
        Map<SocialNetworkEdge, Float> edgeBetweenness = new HashMap<>();
        for(SocialNetworkEdge edge: getEdges()){
            edgeBetweenness.put(edge, 0.0f);
        }

        Collection<SocialNetworkNode> nodes = getGraph().values();

        for (SocialNetworkNode nodeS : nodes) {
            // SINGLE-SOURCE SHORTEST-PATHS
            // INITIALIZATION
            for (SocialNetworkNode node: nodes) {
                predecessor.put(node, new HashSet<>());
                distance.put(node, -1);
                sigma.put(node, 0);
            }
            distance.put(nodeS, 0);
            sigma.put(nodeS, 1);
            queue.add(nodeS);

            while(!queue.isEmpty()){
                SocialNetworkNode nodeV = queue.remove();
                stack.push(nodeV);

                for(SocialNetworkNode nodeW : nodeV.getNeighbors()){
                    // PATH DISCOVERY - nodeW found for the first time?
                    if (distance.get(nodeW).equals(-1)){
                        distance.put(nodeW, distance.get(nodeV) + 1);
                        queue.add(nodeW);
                    }

                    // PATH COUNTING - edge(v, w) on a shortest path?
                    if (distance.get(nodeW).equals(distance.get(nodeV) + 1)){
                        sigma.put(nodeW, sigma.get(nodeW) + sigma.get(nodeV));
                        predecessor.get(nodeW).add(nodeV);
                    }
                }
            }

            // ACCUMULATION -- back-propagation of dependencies
            for (SocialNetworkNode nodeV : nodes) {
                delta.put(nodeV, 0.0f);
            }
            while(!stack.isEmpty()){
                SocialNetworkNode nodeW = stack.pop();
                for(SocialNetworkNode nodeV: predecessor.get(nodeW)){
                    float c = ((float) sigma.get(nodeV)/sigma.get(nodeW)) * (1 + delta.get(nodeW));
                    edgeBetweenness.put(nodeV.getEdgeCorrespondingToNeighbor(nodeW),
                            edgeBetweenness.get(nodeV.getEdgeCorrespondingToNeighbor(nodeW)) + c);
                    delta.put(nodeV, delta.get(nodeV) + c);
                }

                if (!nodeW.equals(nodeS)){
                    nodeBetweenness.put(nodeW, nodeBetweenness.get(nodeW) + delta.get(nodeW));
                }
            }
        }
        reduceBetweennessByHalf(edgeBetweenness);
        updateEdgeBetweenness(edgeBetweenness);
        return edgeBetweenness;
    }

    private void updateEdgeBetweenness(Map<SocialNetworkEdge, Float> edgeBetweenness) {
        for(SocialNetworkEdge edge:getEdges()){
            edge.setBetweenness(edgeBetweenness.get(edge));
        }
    }
}