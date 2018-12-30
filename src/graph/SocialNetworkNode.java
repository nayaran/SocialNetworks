package graph;

import java.util.HashSet;
import java.util.Set;

public class SocialNetworkNode {
	private Integer item;
	private Integer distance;
	private Integer weight;
	private String label;
	private Set<SocialNetworkNode> neighbors;
	private final boolean SHOW_LABEL = true;

	public SocialNetworkNode(Integer item) {
		this.item = item;
		this.distance = null;
		this.weight = null;
		this.label = null;
		this.neighbors = new HashSet<SocialNetworkNode>();
	}

	public SocialNetworkNode(Integer item, String label) {
		this.item = item;
		this.distance = null;
		this.weight = null;
		this.label = label;
		this.neighbors = new HashSet<SocialNetworkNode>();
	}

	public SocialNetworkNode(Integer item, String label, Integer distance, Integer weight) {
		this.item = item;
		this.label = label;
		this.distance = distance;
		this.weight = weight;
		this.neighbors = new HashSet<SocialNetworkNode>();
	}

	public Set<SocialNetworkNode> getNeighbors() {
		return neighbors;
	}

	public void addNeighbor(SocialNetworkNode node) {
		if (neighbors.contains(node)) {
			System.out.println("Neighbor already exists - " + node);
			return;
		}
		neighbors.add(node);
	}

	public void removeNeighbor(SocialNetworkNode neighbor) {
		if (!neighbors.contains(neighbor)) {
			System.out.println("Neighbor doesn't exist - " + neighbor);
			return;
		}
		neighbors.remove(neighbor);
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
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		if (this == other)
			return true;

		SocialNetworkNode otherNode = (SocialNetworkNode) other;

		// if(otherNode.getDistance() == null && distance != null)
		// return false;
		// if(distance == null && otherNode.getDistance() != null)
		// return false;
		// if (otherNode.getDistance() != null && distance != null) {
		// if (!otherNode.getDistance().equals(distance))
		// return false;
		// }
		//
		// if(otherNode.getWeight() == null && weight != null)
		// return false;
		// if(weight == null && otherNode.getWeight() != null)
		// return false;
		// if (otherNode.getWeight() != null && weight != null) {
		// if (!otherNode.getWeight().equals(weight))
		// return false;
		// }

		if (!otherNode.getItem().equals(item))
			return false;

		return true;
	}

	public boolean isExactlyEqual(Object other) {
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		if (this == other)
			return true;

		SocialNetworkNode otherNode = (SocialNetworkNode) other;

		// compare all fields
		if (!otherNode.getItem().equals(item))
			return false;

		if (otherNode.getDistance() == null && distance != null)
			return false;
		if (distance == null && otherNode.getDistance() != null)
			return false;
		if (otherNode.getDistance() != null && distance != null) {
			if (!otherNode.getDistance().equals(distance))
				return false;
		}

		if (otherNode.getWeight() == null && weight != null)
			return false;
		if (weight == null && otherNode.getWeight() != null)
			return false;
		if (otherNode.getWeight() != null && weight != null) {
			if (!otherNode.getWeight().equals(weight))
				return false;
		}

		// compare neighbors
		Set<SocialNetworkNode> neighborsOfOtherNode = otherNode.getNeighbors();
		if (neighbors.size() != neighborsOfOtherNode.size())
			return false;

		for (SocialNetworkNode neighbor : neighbors) {
			if (!neighborsOfOtherNode.contains(neighbor))
				return false;
		}

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
}
