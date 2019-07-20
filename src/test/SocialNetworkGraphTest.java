package test;

import static org.junit.Assert.*;

import java.util.*;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import graph.Graph;
import graph.SocialNetworkEdge;
import graph.SocialNetworkGraph;
import graph.SocialNetworkNode;
import util.GraphLoader;

public class SocialNetworkGraphTest {
	private static final Logger logger = LogManager.getLogger(SocialNetworkGraphTest.class);
	private static Graph testGraph;

	@Before
	public void setUpBeforeClass() throws Exception {
		// Reset the graph before each test as each test has freedom to
		// load its own graph
		testGraph = new SocialNetworkGraph();
	}

	private void loadGraph(Graph graph, String fileName) {
		logger.info("Loading graph from " + fileName);
		long startTime = System.currentTimeMillis();
		GraphLoader.loadGraph(graph, fileName);
		logger.info(graph);
		logger.info("Took {}ms", System.currentTimeMillis() - startTime);
	}

	private SocialNetworkGraph loadTinyGraph() {
		SocialNetworkGraph tinyGraph = new SocialNetworkGraph();
		tinyGraph.addEdge(2, "A", 1, "B");
		tinyGraph.addEdge(2, "A", 4, "C");
		tinyGraph.addEdge(1, "B", 4, "C");
		tinyGraph.addEdge(1, "B", 3, "D");
		tinyGraph.addEdge(3, "D", 4, "C");
		tinyGraph.addEdge(3, "D", 5, "E");
		tinyGraph.addEdge(5, "E", 6, "F");
		tinyGraph.addEdge(5, "E", 7, "G");
		tinyGraph.addEdge(6, "F", 7, "G");
		logger.debug("Graph created");
		logger.debug(tinyGraph);
		return tinyGraph;
	}

	@Test
	public void testNumVertices() {
		logger.info("TEST - testNumVertices");
		loadGraph(testGraph, "data/small_test_graph.txt");
		// Tests for total number of vertices in the graph
		assertEquals(testGraph.exportGraph().size(), 14);
	}

	@Test
	public void testNeighbors() {
		logger.info("TEST - testNeighbors");
		loadGraph(testGraph, "data/small_test_graph.txt");
		// Tests for neighbors connected to 8
		Integer num = 8;
		Set<Integer> expectedEdges = new HashSet<Integer>(Arrays.asList(7, 9, 12));
		HashMap<Integer, HashSet<Integer>> graph = testGraph.exportGraph();
		assertEquals(expectedEdges, graph.get(num));
	}

	@Test
	public void testBFS() {
		logger.info("TEST - testBFS");
		SocialNetworkGraph testGraph = loadTinyGraph();

		List<SocialNetworkNode> expectedPath = new ArrayList<SocialNetworkNode>();
		expectedPath.add(new SocialNetworkNode(2, "A"));
		expectedPath.add(new SocialNetworkNode(1, "B"));
		expectedPath.add(new SocialNetworkNode(3, "D"));
		expectedPath.add(new SocialNetworkNode(5, "E"));
		expectedPath.add(new SocialNetworkNode(7, "G"));

		int source = 2;
		int destination = 7;
		logger.debug("Searching for path between " + source + " and " + destination);

		List<SocialNetworkNode> pathToTest = testGraph.BFS(source, destination);

		String result = pathToTest == null ? "NA" : pathToTest.toString();
		logger.debug("Path - " + result);
		assertEquals(expectedPath, pathToTest);
	}

	@Test
	public void testRemoveEdge() {
		logger.info("TEST - testRemoveEdge");
		SocialNetworkGraph testGraph = loadTinyGraph();
		HashMap<Integer, HashSet<Integer>> graphMap = testGraph.exportGraph();
		SocialNetworkNode firstEnd = new SocialNetworkNode(3, "D");
		SocialNetworkNode secondEnd = new SocialNetworkNode(5, "E");

		SocialNetworkEdge edgeToRemove = new SocialNetworkEdge(firstEnd, secondEnd);
		Set<SocialNetworkEdge> edges = testGraph.getEdges();
		logger.debug("Edges before removal - " + edges);
		logger.debug("Edge to remove - " + edgeToRemove);
		Set<Integer> neighborsOfFirstEnd = graphMap.get(firstEnd.getItem());
		Set<Integer> neighborsOfSecondEnd = graphMap.get(secondEnd.getItem());
		logger.debug("Neighbors of firstEnd before removal - " + neighborsOfFirstEnd);
		logger.debug("Neighbors of secondEnd before removal - " + neighborsOfSecondEnd);

		testGraph.removeEdge(3, 5);

		graphMap = testGraph.exportGraph();

		edges = testGraph.getEdges();
		logger.debug("Edges after removal - " + edges);
		assertFalse(edges.contains(edgeToRemove));

		neighborsOfFirstEnd = testGraph.exportGraph().get(firstEnd.getItem());
		neighborsOfSecondEnd = testGraph.exportGraph().get(secondEnd.getItem());

		logger.debug("Neighbors of firstEnd after removal - " + neighborsOfFirstEnd);
		logger.debug("Neighbors of secondEnd after removal - " + neighborsOfSecondEnd);

		assertFalse(neighborsOfFirstEnd.contains(secondEnd.getItem()));
		assertFalse(neighborsOfSecondEnd.contains(firstEnd.getItem()));
	}

	@Test
	public void testContainsForSocialNetworkNode() {
		logger.info("TEST - testContainsForSocialNetworkNode");
		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(2);
		assertTrue(testGraph.checkNodeForExistence(2));
	}

	@Test
	public void testContainsForSocialNetworkEdge() {
		logger.info("TEST - testContainsForSocialNetworkEdge");
		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addEdge(2, 3);
		assertTrue(testGraph.getEdges().contains(new SocialNetworkEdge(2, 3)));
	}

	@Test
	public void testEqualityForSocialNetworkEdge() {
		logger.info("Test - testEqualityForSocialNetworkEdge");
		SocialNetworkEdge edge1 = new SocialNetworkEdge(2, 3, 2.0f);
		SocialNetworkEdge edge2 = new SocialNetworkEdge(2, 3);
		SocialNetworkEdge edge3 = new SocialNetworkEdge(2, 3);
		SocialNetworkEdge edge4 = new SocialNetworkEdge(3, 2);
		SocialNetworkEdge edge5 = new SocialNetworkEdge(2, 3, 2.0f);

		assertEquals(edge1, edge5);
		assertEquals(edge2, edge3);

		assertEquals(edge1, edge3);
		assertFalse(edge1.isExactlyEqual(edge3));

		assertEquals(edge2, edge4);
		assertEquals(edge2.hashCode(), edge4.hashCode());
	}

	@Test
	public void testUpdateDistanceAndWeights() {
		logger.info("TEST - testUpdateDistanceAndWeights");
		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A");
		testGraph.addVertex(3, "E");
		testGraph.addVertex(2, "B");
		testGraph.addVertex(4, "G");
		testGraph.addVertex(5, "D");
		testGraph.addVertex(6, "F");
		testGraph.addEdge(1, 2);
		testGraph.addEdge(1, 3);
		testGraph.addEdge(2, 4);
		testGraph.addEdge(2, 6);
		testGraph.addEdge(3, 6);
		testGraph.addEdge(6, 5);
		testGraph.addEdge(4, 5);

		logger.debug("testGraph before updateDistanceAndWeights() - " + testGraph);

		SocialNetworkGraph expectedGraph = new SocialNetworkGraph();
		expectedGraph.addVertex(1, "A", 0, 1);
		expectedGraph.addVertex(3, "E", 1, 1);
		expectedGraph.addVertex(2, "B", 1, 1);
		expectedGraph.addVertex(4, "G", 2, 1);
		expectedGraph.addVertex(5, "D", 3, 3);
		expectedGraph.addVertex(6, "F", 2, 2);
		expectedGraph.addEdge(1, 2);
		expectedGraph.addEdge(1, 3);
		expectedGraph.addEdge(2, 4);
		expectedGraph.addEdge(2, 6);
		expectedGraph.addEdge(3, 6);
		expectedGraph.addEdge(6, 5);
		expectedGraph.addEdge(4, 5);

		ArrayList<SocialNetworkNode> leafNodes = new ArrayList<SocialNetworkNode>();
		testGraph.updateDistanceAndWeights(testGraph.getNode(1), leafNodes);

		logger.debug("testGraph after updateDistanceAndWeights() - " + testGraph);
		logger.debug("expectedGraph - " + expectedGraph);

		assertEquals(expectedGraph, testGraph);
	}

	@Test
	public void testUpdateDistanceAndWeightsTest2() {
		logger.info("TEST - testUpdateDistanceAndWeightsTest2");
		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A");
		testGraph.addVertex(2, "B");
		testGraph.addVertex(3, "C");
		testGraph.addVertex(4, "D");
		testGraph.addVertex(5, "E");
		testGraph.addVertex(6, "F");
		testGraph.addVertex(7, "G");

		testGraph.addEdge(1, 2);
		testGraph.addEdge(1, 3);
		testGraph.addEdge(2, 4);
		testGraph.addEdge(3, 4);
		testGraph.addEdge(3, 5);
		testGraph.addEdge(4, 6);
		testGraph.addEdge(5, 6);
		testGraph.addEdge(5, 7);

		logger.debug("testGraph before updateDistanceAndWeights() - " + testGraph);

		SocialNetworkGraph expectedGraph = new SocialNetworkGraph();
		expectedGraph.addVertex(1, "A", 0, 1);
		expectedGraph.addVertex(2, "B", 1, 1);
		expectedGraph.addVertex(3, "C", 1, 1);
		expectedGraph.addVertex(4, "D", 2, 2);
		expectedGraph.addVertex(5, "E", 2, 1);
		expectedGraph.addVertex(6, "F", 3, 3);
		expectedGraph.addVertex(7, "G", 3, 1);
		expectedGraph.addEdge(1, 2);
		expectedGraph.addEdge(1, 3);
		expectedGraph.addEdge(2, 4);
		expectedGraph.addEdge(3, 4);
		expectedGraph.addEdge(3, 5);
		expectedGraph.addEdge(4, 6);
		expectedGraph.addEdge(5, 6);
		expectedGraph.addEdge(5, 7);

		ArrayList<SocialNetworkNode> leafNodes = new ArrayList<SocialNetworkNode>();
		testGraph.updateDistanceAndWeights(testGraph.getNode(1), leafNodes);

		logger.debug("testGraph after updateDistanceAndWeights() - " + testGraph);
		logger.debug("expectedGraph - " + expectedGraph);

		assertEquals(expectedGraph, testGraph);
	}

	@Test
	public void testUpdateEdgeBetweenness() {
		logger.info("TEST - testUpdateEdgeBetweenness");

		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A", 0, 1);
		testGraph.addVertex(2, "B", 1, 1);
		testGraph.addVertex(3, "C", 1, 1);
		testGraph.addVertex(4, "D", 2, 2);
		testGraph.addVertex(5, "E", 2, 1);
		testGraph.addVertex(6, "F", 3, 3);
		testGraph.addVertex(7, "G", 3, 1);

		testGraph.addEdge(1, 2);
		testGraph.addEdge(1, 3);
		testGraph.addEdge(2, 4);
		testGraph.addEdge(3, 4);
		testGraph.addEdge(3, 5);
		testGraph.addEdge(4, 6);
		testGraph.addEdge(5, 6);
		testGraph.addEdge(5, 7);

		logger.debug("testGraph before testUpdateEdgeBetweenness() - " + testGraph);

		SocialNetworkGraph expectedGraph = new SocialNetworkGraph();
		expectedGraph.addVertex(1, "A", 0, 1);
		expectedGraph.addVertex(2, "B", 1, 1);
		expectedGraph.addVertex(3, "C", 1, 1);
		expectedGraph.addVertex(4, "D", 2, 2);
		expectedGraph.addVertex(5, "E", 2, 1);
		expectedGraph.addVertex(6, "F", 3, 3);
		expectedGraph.addVertex(7, "G", 3, 1);
		expectedGraph.addEdge(1, 2, 1.8333334f);
		expectedGraph.addEdge(1, 3, 4.166667f);
		expectedGraph.addEdge(2, 4, 0.8333334f);
		expectedGraph.addEdge(3, 4, 0.8333334f);
		expectedGraph.addEdge(3, 5, 2.3333335f);
		expectedGraph.addEdge(4, 6, 0.6666667f);
		expectedGraph.addEdge(5, 6, 0.33333334f);
		expectedGraph.addEdge(5, 7, 1.0f);

		HashMap<SocialNetworkEdge, Float> expectedEdgeToBetweenessMap = expectedGraph.getEdgeToBetweennessMap();

		SocialNetworkNode endNode = testGraph.getNode(7);
		ArrayList<SocialNetworkNode> leafNodes = new ArrayList<SocialNetworkNode>();
		leafNodes.add(testGraph.getNode(6));
		leafNodes.add(testGraph.getNode(7));

		testGraph.updateEdgeScore(endNode, leafNodes);

		HashMap<SocialNetworkEdge, Float> edgeToBetweenessMapToTest = testGraph.getEdgeToBetweennessMap();

		logger.debug("testGraph after updateDistanceAndWeights() - " + testGraph);
		logger.debug("expectedEdgeToBetweenessMap - " + expectedEdgeToBetweenessMap);
		logger.debug("edgeToBetweenessMapToTest - " + edgeToBetweenessMapToTest);

		assertTrue(testEdgeToBetweennessMapsForEquality(expectedEdgeToBetweenessMap, edgeToBetweenessMapToTest));
	}

	@Test
	public void testComputeBetweeness() {
		logger.info("TEST - testComputeBetweeness");

		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A");
		testGraph.addVertex(2, "B");
		testGraph.addVertex(3, "D");
		testGraph.addVertex(4, "C");

		testGraph.addEdge(1, 2);
		testGraph.addEdge(2, 4);
		testGraph.addEdge(1, 3);
		testGraph.addEdge(3, 4);

		logger.debug("testGraph before testUpdateEdgeBetweenness() - " + testGraph);

		SocialNetworkGraph expectedGraph = new SocialNetworkGraph();
		expectedGraph.addVertex(1, "A");
		expectedGraph.addVertex(2, "B");
		expectedGraph.addVertex(3, "D");
		expectedGraph.addVertex(4, "C");

		expectedGraph.addEdge(1, 2, 2.0f);
		expectedGraph.addEdge(2, 4, 2.0f);
		expectedGraph.addEdge(1, 3, 2.0f);
		expectedGraph.addEdge(3, 4, 2.0f);

		HashMap<SocialNetworkEdge, Float> expectedEdgeToBetweenessMap = expectedGraph.getEdgeToBetweennessMap();

		HashMap<SocialNetworkEdge, Float> edgeToBetweenessMapToTest = testGraph.computeBetweenness();

		logger.debug("testGraph after testComputeBetweeness() - " + testGraph);
		logger.debug("expectedEdgeToBetweenessMap - " + expectedEdgeToBetweenessMap);
		logger.debug("edgeToBetweenessMapToTest - " + edgeToBetweenessMapToTest);

		assertTrue(testEdgeToBetweennessMapsForEquality(expectedEdgeToBetweenessMap, edgeToBetweenessMapToTest));
	}

	@Test
	public void testComputeBetweenessTest2() {
		logger.info("TEST - testComputeBetweenessTest2");

		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A");
		testGraph.addVertex(2, "B");
		testGraph.addVertex(3, "C");
		testGraph.addVertex(4, "D");
		testGraph.addVertex(5, "E");
		testGraph.addVertex(6, "F");

		testGraph.addEdge(1, 2);
		testGraph.addEdge(1, 4);
		testGraph.addEdge(2, 3);
		testGraph.addEdge(2, 5);
		testGraph.addEdge(3, 6);
		testGraph.addEdge(5, 6);
		testGraph.addEdge(4, 5);

		logger.debug("testGraph before testUpdateEdgeBetweenness() - " + testGraph);

		SocialNetworkGraph expectedGraph = new SocialNetworkGraph();
		expectedGraph.addVertex(1, "A");
		expectedGraph.addVertex(2, "B");
		expectedGraph.addVertex(3, "C");
		expectedGraph.addVertex(4, "D");
		expectedGraph.addVertex(5, "E");
		expectedGraph.addVertex(6, "F");

		expectedGraph.addEdge(1, 2, 4.0f);
		expectedGraph.addEdge(1, 4, 2.67f);
		expectedGraph.addEdge(2, 3, 4.0f);
		expectedGraph.addEdge(2, 5, 3.67f);
		expectedGraph.addEdge(3, 6, 2.67f);
		expectedGraph.addEdge(5, 6, 4.0f);
		expectedGraph.addEdge(4, 5, 4.0f);

		HashMap<SocialNetworkEdge, Float> expectedEdgeToBetweenessMap = expectedGraph.getEdgeToBetweennessMap();

		HashMap<SocialNetworkEdge, Float> edgeToBetweenessMapToTest = testGraph.computeBetweenness();

		logger.debug("testGraph after testComputeBetweeness() - " + testGraph);
		logger.debug("expectedEdgeToBetweenessMap - " + expectedEdgeToBetweenessMap);
		logger.debug("edgeToBetweenessMapToTest - " + edgeToBetweenessMapToTest);

		assertTrue(testEdgeToBetweennessMapsForEquality(expectedEdgeToBetweenessMap, edgeToBetweenessMapToTest));
	}

	private boolean testEdgeToBetweennessMapsForEquality(Map<SocialNetworkEdge, Float> map1,
			Map<SocialNetworkEdge, Float> map2) {
		if (map1.size() != map2.size())
			return false;
		for (Entry<SocialNetworkEdge, Float> entry : map1.entrySet()) {
			SocialNetworkEdge edgeFromMap1 = entry.getKey();
			Float betweennessOfEdgeFromMap1 = entry.getValue();
			Float betweennessOfCorrespondingEdgeFromMap2 = map2.get(edgeFromMap1);

			if (betweennessOfCorrespondingEdgeFromMap2 == null) {
				logger.debug("Unequal for edge- " + edgeFromMap1);
				return false;
			}
			if (!betweennessOfEdgeFromMap1.equals(betweennessOfCorrespondingEdgeFromMap2)) {
				logger.debug("Unequal for edge- " + edgeFromMap1);
				return false;
			}
		}
		return true;
	}

	@Test
	public void testGetConnectedComponents() {
		logger.info("TEST - testGetConnectedComponents");

		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A");
		testGraph.addVertex(2, "B");
		testGraph.addVertex(3, "D");
		testGraph.addVertex(4, "C");

		testGraph.addEdge(1, 2);
		testGraph.addEdge(2, 4);
		testGraph.addEdge(1, 3);
		testGraph.addEdge(3, 4);

		logger.debug("testGraph before getConnectedComponents() - " + testGraph);

		List<SocialNetworkGraph> connectedComponents = testGraph.getConnectedComponents();
		logger.debug("TEST - 1 Connection components without modificaiton");
		logger.debug("Connected components - " + connectedComponents);

		assertEquals(1, connectedComponents.size());

		// Remove an edge
		logger.debug("TEST - 2 Removing edges");
		testGraph.removeEdge(2, 4);
		testGraph.removeEdge(3, 4);

		connectedComponents = testGraph.getConnectedComponents();
		logger.debug("Connected components - " + connectedComponents);
		assertEquals(2, connectedComponents.size());
	}

	@Test
	public void testGetCommunities() {
		logger.info("TEST - testGetCommunities");

		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A");
		testGraph.addVertex(2, "B");
		testGraph.addVertex(3, "D");
		testGraph.addVertex(4, "C");

		testGraph.addEdge(1, 2);
		testGraph.addEdge(2, 4);
		testGraph.addEdge(1, 3);
		testGraph.addEdge(3, 4);

		logger.debug("testGraph - " + testGraph);

		List<SocialNetworkGraph> communities = testGraph.getConnectedComponents();
		logger.debug("TEST - 1 Connected components before community detection");
		logger.debug("Communities - " + communities);

		assertEquals(1, communities.size());

		// Detect communities
		logger.debug("TEST - 2 Detecting communities");
		Integer depth = 1;
		communities = testGraph.getCommunities(depth);

		logger.debug("Communities - " + communities);
		assertEquals(4, communities.size());
	}

	@Test
	public void testGetCommunitiesTest2() {
		logger.info("TEST - testGetCommunitiesTest2");

		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A");
		testGraph.addVertex(2, "B");
		testGraph.addVertex(3, "D");
		testGraph.addVertex(4, "C");
		testGraph.addVertex(5, "E");
		testGraph.addVertex(6, "F");
		testGraph.addVertex(7, "G");

		testGraph.addEdge(1, 2);
		testGraph.addEdge(1, 3);
		testGraph.addEdge(1, 4);
		testGraph.addEdge(2, 4);
		testGraph.addEdge(3, 4);
		testGraph.addEdge(3, 5);
		testGraph.addEdge(5, 6);
		testGraph.addEdge(5, 7);
		testGraph.addEdge(6, 7);

		logger.debug("testGraph - " + testGraph);

		List<SocialNetworkGraph> communities = testGraph.getConnectedComponents();

		// Detect communities
		logger.debug("TEST - 1 Detecting communities with iterations 1");
		Integer depth = 1;
		communities = testGraph.getCommunities(depth);

		logger.debug("Communities - " + communities);
		assertEquals(2, communities.size());

		// Detect communities
		logger.debug("TEST - 2 Detecting communities with iterations 2");
		depth = 2;
		communities = testGraph.getCommunities(depth);

		logger.debug("Communities - " + communities);
		assertEquals(4, communities.size());
	}

	@Test
	public void testGetCommunitiesTestFacebook2000() {
		logger.info("TEST - testGetCommunitiesTestBig");
		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		loadGraph(testGraph, "data/facebook_2000.txt");

		logger.debug("testGraph - " + testGraph);

		List<SocialNetworkGraph> communities;

		// Detect communities
		logger.debug("TEST - 2 Detecting communities with depth 1");
		int depth = 2;
		communities = testGraph.getCommunitiesUsingBrandes(depth);

		logger.debug("Communities - " + communities);
		assertEquals(13, communities.size());

	}

	@Test
	public void testGetCommunitiesUsingBrandesForSmallTestGraph() {
		logger.info("TEST - testGetCommunitiesForSmallTestGraph");
		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		loadGraph(testGraph, "data/small_test_graph.txt");

		logger.debug("testGraph - " + testGraph);

		List<SocialNetworkGraph> communities = testGraph.getConnectedComponents();

		// Detect communities
		logger.debug("TEST - 1 Detecting communities with depth 1");
		Integer depth = 1;
		communities = testGraph.getCommunitiesUsingBrandes(depth);

		logger.debug("Communities - " + communities);
		assertEquals(2, communities.size());

		// Detect communities
		logger.debug("TEST - 2 Detecting communities with depth 2");
		depth = 1;
		communities = testGraph.getCommunitiesUsingBrandes(depth);

		logger.debug("Communities - " + communities);
		assertEquals(6, communities.size());

	}

	@Test
	public void testComputeBetweenessUsingBrandes() {
		logger.info("TEST - testComputeBetweenessUsingBrandes");

		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A");
		testGraph.addVertex(2, "B");
		testGraph.addVertex(3, "D");
		testGraph.addVertex(4, "C");

		testGraph.addEdge(1, 2);
		testGraph.addEdge(2, 4);
		testGraph.addEdge(1, 3);
		testGraph.addEdge(3, 4);

		logger.debug("testGraph before testUpdateEdgeBetweenness() - " + testGraph);

		SocialNetworkGraph expectedGraph = new SocialNetworkGraph();
		expectedGraph.addVertex(1, "A");
		expectedGraph.addVertex(2, "B");
		expectedGraph.addVertex(3, "D");
		expectedGraph.addVertex(4, "C");

		expectedGraph.addEdge(1, 2, 2.0f);
		expectedGraph.addEdge(2, 4, 2.0f);
		expectedGraph.addEdge(1, 3, 2.0f);
		expectedGraph.addEdge(3, 4, 2.0f);

		HashMap<SocialNetworkEdge, Float> expectedEdgeToBetweenessMap = expectedGraph.getEdgeToBetweennessMap();

		Map<SocialNetworkEdge, Float> edgeToBetweenessMapToTest = testGraph.computeBetweennessUsingBrandes();

		logger.debug("testGraph after testComputeBetweeness() - " + testGraph);
		logger.debug("expectedEdgeToBetweenessMap - " + expectedEdgeToBetweenessMap);
		logger.debug("edgeToBetweenessMapToTest - " + edgeToBetweenessMapToTest);

		assertTrue(testEdgeToBetweennessMapsForEquality(expectedEdgeToBetweenessMap, edgeToBetweenessMapToTest));
	}

	@Test
	public void testComputeBetweenessUsingBrandesTest2() {
		logger.info("TEST - testComputeBetweenessUsingBrandesTest2");

		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A");
		testGraph.addVertex(2, "B");
		testGraph.addVertex(3, "C");
		testGraph.addVertex(4, "D");
		testGraph.addVertex(5, "E");
		testGraph.addVertex(6, "F");

		testGraph.addEdge(1, 2);
		testGraph.addEdge(1, 4);
		testGraph.addEdge(2, 3);
		testGraph.addEdge(2, 5);
		testGraph.addEdge(3, 6);
		testGraph.addEdge(5, 6);
		testGraph.addEdge(4, 5);

		logger.debug("testGraph before testUpdateEdgeBetweenness() - " + testGraph);

		SocialNetworkGraph expectedGraph = new SocialNetworkGraph();
		expectedGraph.addVertex(1, "A");
		expectedGraph.addVertex(2, "B");
		expectedGraph.addVertex(3, "C");
		expectedGraph.addVertex(4, "D");
		expectedGraph.addVertex(5, "E");
		expectedGraph.addVertex(6, "F");

		expectedGraph.addEdge(1, 2, 4.0f);
		expectedGraph.addEdge(1, 4, 2.67f);
		expectedGraph.addEdge(2, 3, 4.0f);
		expectedGraph.addEdge(2, 5, 3.67f);
		expectedGraph.addEdge(3, 6, 2.67f);
		expectedGraph.addEdge(5, 6, 4.0f);
		expectedGraph.addEdge(4, 5, 4.0f);

		HashMap<SocialNetworkEdge, Float> expectedEdgeToBetweenessMap = expectedGraph.getEdgeToBetweennessMap();

		Map<SocialNetworkEdge, Float> edgeToBetweenessMapToTest = testGraph.computeBetweennessUsingBrandes();

		logger.debug("testGraph after testComputeBetweeness() - " + testGraph);
		logger.debug("expectedEdgeToBetweenessMap - " + expectedEdgeToBetweenessMap);
		logger.debug("edgeToBetweenessMapToTest - " + edgeToBetweenessMapToTest);

		assertTrue(testEdgeToBetweennessMapsForEquality(expectedEdgeToBetweenessMap, edgeToBetweenessMapToTest));
	}
	@Test
	public void testGetAtLeastNCommunitiesUsingBrandes() {
		logger.info("TEST - testGetAtLeastNCommunitiesUsingBrandes");

		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A");
		testGraph.addVertex(2, "B");
		testGraph.addVertex(3, "D");
		testGraph.addVertex(4, "C");
		testGraph.addVertex(5, "E");
		testGraph.addVertex(6, "F");
		testGraph.addVertex(7, "G");

		testGraph.addEdge(1, 2);
		testGraph.addEdge(1, 3);
		testGraph.addEdge(1, 4);
		testGraph.addEdge(2, 4);
		testGraph.addEdge(3, 4);
		testGraph.addEdge(3, 5);
		testGraph.addEdge(5, 6);
		testGraph.addEdge(5, 7);
		testGraph.addEdge(6, 7);

		logger.debug("testGraph before testGetAtLeastNCommunitiesUsingBrandes() - " + testGraph);

		Integer expectedCommunities = 2;
		List<SocialNetworkGraph> actualCommunities = testGraph.getAtLeastNCommunitiesUsingBrandes(2);

		Assert.assertEquals(expectedCommunities.intValue(), actualCommunities.size());
	}

	@Test
	public void testGetCommunitiesUsingBrandesForKarateClub() {
		logger.info("TEST - testGetCommunitiesUsingBrandesForKarateClub");
		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		loadGraph(testGraph, "data/karate.txt");

		logger.debug("testGraph - " + testGraph);

		Integer expectedCommunities = 2;
		List<SocialNetworkGraph> actualCommunities = testGraph.getAtLeastNCommunitiesUsingBrandes(2);

		Assert.assertEquals(expectedCommunities.intValue(), actualCommunities.size());
	}
}