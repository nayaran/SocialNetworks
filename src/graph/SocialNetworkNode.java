package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SocialNetworkNode {
	private Integer item;
	private Integer distance;
	private Integer weight;
	private String label;
	private Set<SocialNetworkNode> neighbors;
	private HashMap<SocialNetworkNode, SocialNetworkEdge> neighborToEdgeMap;
	private final boolean SHOW_LABEL = true;

	public SocialNetworkNode(Integer item) {
		this.item = item;
		this.distance = null;
		this.weight = null;
		this.label = null;
		this.neighbors = new HashSet<SocialNetworkNode>();
		this.neighborToEdgeMap = new HashMap<SocialNetworkNode, SocialNetworkEdge>();
	}

	public SocialNetworkNode(Integer item, String label) {
		this.item = item;
		this.distance = null;
		this.weight = null;
		this.label = label;
		this.neighbors = new HashSet<SocialNetworkNode>();
		this.neighborToEdgeMap = new HashMap<SocialNetworkNode, SocialNetworkEdge>();
	}

	public SocialNetworkNode(Integer item, String label, Integer distance, Integer weight) {
		this.item = item;
		this.label = label;
		this.distance = distance;
		this.weight = weight;
		this.neighbors = new HashSet<SocialNetworkNode>();
		this.neighborToEdgeMap = new HashMap<SocialNetworkNode, SocialNetworkEdge>();
	}

	public Set<SocialNetworkNode> getNeighbors() {
		return neighbors;
	}

	public Collection<SocialNetworkEdge> getEdges() {
		return neighborToEdgeMap.values();
	}

	public void addNeighbor(SocialNetworkNode node) {
		if (neighbors.contains(node)) {
			System.out.println("Neighbor already exists - " + node);
			return;
		}
		neighbors.add(node);
		if (neighborToEdgeMap.get(node) == null) {
			neighborToEdgeMap.put(node, new SocialNetworkEdge(this, node));
		}
	}

	public void addNeighbor(SocialNetworkNode node, float betweenness) {
		if (neighbors.contains(node)) {
			System.out.println("Neighbor already exists - " + node);
			return;
		}
		neighbors.add(node);
		if (neighborToEdgeMap.get(node) == null) {
			neighborToEdgeMap.put(node, new SocialNetworkEdge(this, node, betweenness));
		}
	}

	public SocialNetworkEdge getEdgeCorrespondingToNeighbor(SocialNetworkNode neighbor) {
		if (!neighbors.contains(neighbor)) {
			System.out.println("Neighbor doesn't exist - " + neighbor);
		}
		return neighborToEdgeMap.get(neighbor);
	}

	public void removeNeighbor(SocialNetworkNode neighbor) {
		if (!neighbors.contains(neighbor)) {
			System.out.println("Neighbor doesn't exist - " + neighbor);
			return;
		}
		neighbors.remove(neighbor);
		neighborToEdgeMap.remove(neighbor);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer node) {
		this.item = node;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Override
	public boolean equals(Object other) {
		// Basic checks
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		if (this == other)
			return true;

		// Check if both nodes have same item
		SocialNetworkNode otherNode = (SocialNetworkNode) other;
		if (!otherNode.getItem().equals(item))
			return false;

		// Note that for basic equality, we dont' look at other fields
		// For deeper check, use isExactlyEqual

		return true;
	}

	public boolean isExactlyEqual(Object other) {
		// Basic checks
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		if (this == other)
			return true;

		SocialNetworkNode otherNode = (SocialNetworkNode) other;

		// Compare item
		if (!otherNode.getItem().equals(item))
			return false;

		// Compare distance
		if (otherNode.getDistance() == null && distance != null)
			return false;
		if (distance == null && otherNode.getDistance() != null)
			return false;
		if (otherNode.getDistance() != null && distance != null) {
			if (!otherNode.getDistance().equals(distance))
				return false;
		}
		// Compare weight
		if (otherNode.getWeight() == null && weight != null)
			return false;
		if (weight == null && otherNode.getWeight() != null)
			return false;
		if (otherNode.getWeight() != null && weight != null) {
			if (!otherNode.getWeight().equals(weight))
				return false;
		}

		// Compare neighbors
		Set<SocialNetworkNode> neighborsOfOtherNode = otherNode.getNeighbors();
		if (neighbors.size() != neighborsOfOtherNode.size())
			return false;

		for (SocialNetworkNode neighbor : neighbors) {
			if (!neighborsOfOtherNode.contains(neighbor))
				return false;
		}
		// TODO figure out how to implement comparison of edges
		// without stackoverflow error
		return true;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + item.hashCode();
		return result;
	}

	@Override
	public String toString() {
		String s = "";
		String distanceString = distance == null ? "" : "|d=" + distance;
		String weightString = weight == null ? "" : "|w=" + weight;
		s += distanceString + weightString;
		String labelString = label == null ? "" : "(" + label + ")";
		if (SHOW_LABEL && !labelString.equals("")) {
			s = labelString + s;
		}
		s = item.toString() + s;
		return s;
	}

	public void resetNode() {
		weight = null;
		distance = null;

		for (SocialNetworkEdge edge : getEdges())
			edge.resetScore();
	}
}
