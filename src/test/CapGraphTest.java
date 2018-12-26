package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import graph.CapGraph;
import graph.Graph;
import util.GraphLoader;

class CapGraphTest {
	private static  Graph testGraph;

	@BeforeEach
	void setUpBeforeClass() throws Exception {
		// Reset the graph before each test as each test has freedom to
		// load its own graph
		testGraph = new CapGraph();
	}

	private void loadGraph(Graph graph, String fileName) {
		GraphLoader.loadGraph(graph, fileName);
		System.out.print("Loaded " + fileName);
		System.out.println(graph);
	}

	@Test
	void testNumVertices() {
		System.out.println("\nTEST - testNumVertices");
		loadGraph(testGraph, "data/small_test_graph.txt");
		// Tests for total number of vertices in the graph
		assertEquals(testGraph.exportGraph().size(), 14);
	}

	@Test
	void testEdgeForSampleNode() {
		System.out.println("\nTEST - testEdgeForSampleNode");
		loadGraph(testGraph, "data/small_test_graph.txt");
		// Tests for edges connected to 8
		Integer nodeToTest = 8;
		Set<Integer> expectedEdges = new HashSet<Integer>(Arrays.asList(7,9,12));
		assertEquals(expectedEdges, testGraph.exportGraph().get(nodeToTest));
	}

	@Test
	void testGetEgonet() {
		// Test on a smaller dataset
		System.out.println("\nTEST - testGetEgonet");
		loadGraph(testGraph, "data/test_data_for_egonet.txt");

		// Construct expected ego net for 3
		Graph expectedGraph = new CapGraph();
		expectedGraph.addVertex(2);
		expectedGraph.addVertex(3);
		expectedGraph.addVertex(4);
		expectedGraph.addVertex(6);
		expectedGraph.addEdge(3, 2);
		expectedGraph.addEdge(3, 6);
		expectedGraph.addEdge(3, 4);
		expectedGraph.addEdge(2, 4);
		expectedGraph.addEdge(2, 3);
		expectedGraph.addEdge(4, 2);
		expectedGraph.addEdge(4, 3);
		expectedGraph.addEdge(6, 3);
		System.out.println("Testing egonet on test_data_for_egonet.txt");
		System.out.println("Egonet for 3 - ");
		System.out.println(expectedGraph);

		HashMap<Integer, HashSet<Integer>> expectedEgonet = expectedGraph.exportGraph();
		System.out.println("Expected Egonet - " + expectedEgonet);

		// Get ego net for 3 to test
		HashMap<Integer, HashSet<Integer>> egonetToTest = testGraph.getEgonet(3).exportGraph();
		System.out.println("Egonet under test - " + egonetToTest);

		// Compare expected and received ego net
		boolean result = CapGraph.doGraphsMatch(egonetToTest, expectedEgonet);
		if (result == false) {
			assertFalse(true, "Generated egonet does not match expected ego net");
		}
	}
}
