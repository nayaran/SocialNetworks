package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import graph.Graph;
import graph.SocialNetworkEdge;
import graph.SocialNetworkGraph;
import graph.SocialNetworkNode;
import util.GraphLoader;

public class SocialNetworkGraphTest {
	private static Graph testGraph;

	@BeforeEach
	void setUpBeforeClass() throws Exception {
		// Reset the graph before each test as each test has freedom to
		// load its own graph
		testGraph = new SocialNetworkGraph();
	}

	private void loadGraph(Graph graph, String fileName) {
		GraphLoader.loadGraph(graph, fileName);
		System.out.print("Loaded " + fileName);
		System.out.println(graph);
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
		System.out.print("Graph created");
		System.out.println(tinyGraph);
		return tinyGraph;
	}

	@Test
	void testNumVertices() {
		System.out.println("\nTEST - testNumVertices");
		loadGraph(testGraph, "data/small_test_graph.txt");
		// Tests for total number of vertices in the graph
		assertEquals(testGraph.exportGraph().size(), 14);
	}

	@Test
	void testNeighbors() {
		System.out.println("\nTEST - testNeighbors");
		loadGraph(testGraph, "data/small_test_graph.txt");
		// Tests for neighbors connected to 8
		Integer num = 8;
		Set<Integer> expectedEdges = new HashSet<Integer>(Arrays.asList(7, 9, 12));
		HashMap<Integer, HashSet<Integer>> graph = testGraph.exportGraph();
		assertEquals(expectedEdges, graph.get(num));
	}

	@Test
	void testBFS() {
		System.out.println("\nTEST - testBFS");
		SocialNetworkGraph testGraph = loadTinyGraph();

		List<SocialNetworkNode> expectedPath = new ArrayList<SocialNetworkNode>();
		expectedPath.add(new SocialNetworkNode(2, "A"));
		expectedPath.add(new SocialNetworkNode(1, "B"));
		expectedPath.add(new SocialNetworkNode(3, "D"));
		expectedPath.add(new SocialNetworkNode(5, "E"));
		expectedPath.add(new SocialNetworkNode(7, "G"));

		int source = 2;
		int destination = 7;
		System.out.println("Searching for path between " + source + " and " + destination);

		List<SocialNetworkNode> pathToTest = testGraph.BFS(source, destination);

		String result = pathToTest == null ? "NA" : pathToTest.toString();
		System.out.println("Path - " + result);
		assertEquals(expectedPath, pathToTest);
	}

	@Test
	void testRemoveEdge() {
		System.out.println("\nTEST - testRemoveEdge");
		SocialNetworkGraph testGraph = loadTinyGraph();
		HashMap<Integer, HashSet<Integer>> graphMap = testGraph.exportGraph();
		SocialNetworkNode firstEnd = new SocialNetworkNode(3, "D");
		SocialNetworkNode secondEnd = new SocialNetworkNode(5, "E");

		SocialNetworkEdge edgeToRemove = new SocialNetworkEdge(firstEnd, secondEnd);
		Set<SocialNetworkEdge> edges = testGraph.getEdges();
		System.out.println("Edges before removal - " + edges);
		System.out.println("Edge to remove - " + edgeToRemove);
		Set<Integer> neighborsOfFirstEnd = graphMap.get(firstEnd.getItem());
		Set<Integer> neighborsOfSecondEnd = graphMap.get(secondEnd.getItem());
		System.out.println("Neighbors of firstEnd before removal - " + neighborsOfFirstEnd);
		System.out.println("Neighbors of secondEnd before removal - " + neighborsOfSecondEnd);

		testGraph.removeEdge(3, 5);

		graphMap = testGraph.exportGraph();

		edges = testGraph.getEdges();
		System.out.println("Edges after removal - " + edges);
		assertFalse(edges.contains(edgeToRemove));

		neighborsOfFirstEnd = testGraph.exportGraph().get(firstEnd.getItem());
		neighborsOfSecondEnd = testGraph.exportGraph().get(secondEnd.getItem());

		System.out.println("Neighbors of firstEnd after removal - " + neighborsOfFirstEnd);
		System.out.println("Neighbors of secondEnd after removal - " + neighborsOfSecondEnd);

		assertFalse(neighborsOfFirstEnd.contains(secondEnd.getItem()));
		assertFalse(neighborsOfSecondEnd.contains(firstEnd.getItem()));
	}

	@Test
	void testContainsForSocialNetworkNode() {
		System.out.println("\nTEST - testContainsForSocialNetworkNode");
		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(2);
		assertTrue(testGraph.checkNodeForExistence(2));
	}

	@Test
	void testContainsForSocialNetworkEdge() {
		System.out.println("\nTEST - testContainsForSocialNetworkEdge");
		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addEdge(2, 3);
		assertTrue(testGraph.getEdges().contains(new SocialNetworkEdge(2, 3)));
	}

	@Test
	void testEqualityForSocialNetworkEdge() {
		System.out.println("\nTest - testEqualityForSocialNetworkEdge");
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
	void testUpdateDistanceAndWeights() {
		System.out.println("\nTEST - testUpdateDistanceAndWeights");
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

		System.out.println("testGraph before updateDistanceAndWeights() - " + testGraph);

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

		testGraph.updateDistanceAndWeights(testGraph.getNode(1));

		System.out.println("\ntestGraph after updateDistanceAndWeights() - " + testGraph);
		System.out.println("expectedGraph - " + expectedGraph);

		assertEquals(expectedGraph, testGraph);
	}

	@Test
	void testUpdateDistanceAndWeightsTest2() {
		System.out.println("\nTEST - testUpdateDistanceAndWeightsTest2");
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

		System.out.println("testGraph before updateDistanceAndWeights() - " + testGraph);

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

		testGraph.updateDistanceAndWeights(testGraph.getNode(1));

		System.out.println("\ntestGraph after updateDistanceAndWeights() - " + testGraph);
		System.out.println("expectedGraph - " + expectedGraph);

		assertEquals(expectedGraph, testGraph);
	}

	@Test
	void testUpdateEdgeBetweenness() {
		System.out.println("\nTEST - testUpdateEdgeBetweenness");

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

		System.out.println("testGraph before testUpdateEdgeBetweenness() - " + testGraph);

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

		System.out.println("\ntestGraph after updateDistanceAndWeights() - " + testGraph);
		System.out.println("expectedEdgeToBetweenessMap - " + expectedEdgeToBetweenessMap);
		System.out.println("edgeToBetweenessMapToTest - " + edgeToBetweenessMapToTest);

		assertTrue(testEdgeToBetweennessMapsForEquality(expectedEdgeToBetweenessMap, edgeToBetweenessMapToTest));
	}

	private boolean testEdgeToBetweennessMapsForEquality(HashMap<SocialNetworkEdge, Float> map1,
			HashMap<SocialNetworkEdge, Float> map2) {
		if (map1.size() != map2.size())
			return false;
		for (Entry<SocialNetworkEdge, Float> entry : map1.entrySet()) {
			SocialNetworkEdge edgeFromMap1 = entry.getKey();
			Float betweennessOfEdgeFromMap1 = entry.getValue();
			Float betweennessOfCorrespondingEdgeFromMap2 = map2.get(edgeFromMap1);

			if (betweennessOfCorrespondingEdgeFromMap2 == null) {
				System.out.println("Unequal for edge- " + edgeFromMap1);
				return false;
			}
			if (!betweennessOfEdgeFromMap1.equals(betweennessOfCorrespondingEdgeFromMap2)) {
				System.out.println("Unequal for edge- " + edgeFromMap1);
				return false;
			}
		}
		return true;
	}
}
