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

	public SocialNetworkEdge(int firstItem, int secondItem) {
		this.firstEnd = new SocialNetworkNode(firstItem);
		this.secondEnd = new SocialNetworkNode(secondItem);
		this.betweenness = null;
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
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		if (this == other)
			return true;

		SocialNetworkEdge otherEdge = (SocialNetworkEdge) other;

		if (otherEdge.firstEnd.equals(firstEnd) && otherEdge.secondEnd.equals(secondEnd))
			return true;
		else if (otherEdge.firstEnd.equals(secondEnd) && otherEdge.secondEnd.equals(firstEnd))
			return true;
		else
			return false;
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
