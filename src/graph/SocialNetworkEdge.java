package graph;

public class SocialNetworkEdge {
	private SocialNetworkNode firstEnd;
	private SocialNetworkNode secondEnd;
	private Float betweenness;
	private final boolean SHOW_LABEL = true;

	public SocialNetworkEdge(SocialNetworkNode firstEnd, SocialNetworkNode secondEnd) {
		this.firstEnd = firstEnd;
		this.secondEnd = secondEnd;
		this.betweenness = null;
	}

	public SocialNetworkEdge(SocialNetworkNode firstEnd, SocialNetworkNode secondEnd, float betweenness) {
		this.firstEnd = firstEnd;
		this.secondEnd = secondEnd;
		this.betweenness = betweenness;
	}

	public SocialNetworkEdge(int firstItem, int secondItem) {
		this.firstEnd = new SocialNetworkNode(firstItem);
		this.secondEnd = new SocialNetworkNode(secondItem);
		this.betweenness = null;
	}

	public SocialNetworkEdge(int firstItem, int secondItem, float betweenness) {
		this.firstEnd = new SocialNetworkNode(firstItem);
		this.secondEnd = new SocialNetworkNode(secondItem);
		this.betweenness = betweenness;
	}

	public SocialNetworkEdge(int firstItem, String firstLabel, int secondItem, String secondLabel) {
		this.firstEnd = new SocialNetworkNode(firstItem, firstLabel);
		this.secondEnd = new SocialNetworkNode(secondItem, secondLabel);
		this.betweenness = null;
	}

	public SocialNetworkNode getFirstEnd() {
		return firstEnd;
	}

	public void setFirstEnd(SocialNetworkNode firstEnd) {
		this.firstEnd = firstEnd;
	}

	public SocialNetworkNode getSecondEnd() {
		return secondEnd;
	}

	public void setSecondEnd(SocialNetworkNode secondEnd) {
		this.secondEnd = secondEnd;
	}

	public Float getBetweenness() {
		return betweenness;
	}

	public void setBetweenness(Float betweenness) {
		this.betweenness = betweenness;
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

		// Check if both the edges contain same two ends - order doesn't matter
		SocialNetworkEdge otherEdge = (SocialNetworkEdge) other;
		if (otherEdge.firstEnd.equals(firstEnd) && otherEdge.secondEnd.equals(secondEnd))
			return true;

		if (otherEdge.firstEnd.equals(secondEnd) && otherEdge.secondEnd.equals(firstEnd))
			return true;

		return false;
		// Note that for basic equality, we dont' look at other fields - betweenness etc
		// For deeper check, use isExactlyEqual
	}

	public boolean isExactlyEqual(Object other) {
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		if (this == other)
			return true;

		SocialNetworkEdge otherEdge = (SocialNetworkEdge) other;
		SocialNetworkNode firstEndOfOtherEdge = otherEdge.getFirstEnd();
		SocialNetworkNode secondEndOfOtherEdge = otherEdge.getSecondEnd();

		// compare both ends of both edges
		if (!firstEnd.isExactlyEqual(firstEndOfOtherEdge) && !firstEnd.isExactlyEqual(secondEndOfOtherEdge))
			return false;
		if (!secondEnd.isExactlyEqual(secondEndOfOtherEdge) && !secondEnd.isExactlyEqual(firstEndOfOtherEdge))
			return false;

		// compare fields
		if (getBetweenness() == null && otherEdge.getBetweenness() != null)
			return false;
		if (otherEdge.getBetweenness() == null && getBetweenness() != null)
			return false;
		if (!getBetweenness().equals(otherEdge.getBetweenness()))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + firstEnd.hashCode();
		result = 31 * result + secondEnd.hashCode();

		return result;
	}

	@Override
	public String toString() {
		String s = "";
		String betweennessString = betweenness == null ? "" : " | b=" + betweenness;
		String firstEndItem = firstEnd.getItem().toString();
		String secondEndItem = secondEnd.getItem().toString();

		if (SHOW_LABEL) {
			String firstEndLabel = firstEnd.getLabel() == null ? firstEndItem : firstEnd.getLabel();
			String secondEndLabel = secondEnd.getLabel() == null ? secondEndItem : secondEnd.getLabel();
			s += firstEndLabel + "->" + secondEndLabel + betweennessString;
		} else
			s += firstEndItem + "->" + secondEndItem + betweennessString;

		return s;
	}
}
