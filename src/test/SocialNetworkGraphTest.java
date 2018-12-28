package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

		testGraph.removeEdge(edgeToRemove);

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
		SocialNetworkEdge edge = new SocialNetworkEdge(2, 3);
		testGraph.addEdge(edge);
		assertTrue(testGraph.getEdges().contains(edge));
	}

	@Test
	void testAssignWeights() {
		System.out.println("\nTEST - testAssignWeights");
		SocialNetworkGraph testGraph = new SocialNetworkGraph();
		testGraph.addVertex(1, "A", 0, 1);
		testGraph.addVertex(3, "E", 1, 1);
		testGraph.addVertex(2, "B", 1, 1);
		testGraph.addVertex(4, "G", 2, 1);
		testGraph.addVertex(5, "D", 3, 3);
		testGraph.addVertex(6, "F", 2, 2);

		testGraph.addEdge(1, 2);
		testGraph.addEdge(1, 3);
		testGraph.addEdge(2, 4);
		testGraph.addEdge(3, 6);
		testGraph.addEdge(6, 5);
		testGraph.addEdge(4, 5);
		System.out.println(testGraph);
	}
}
