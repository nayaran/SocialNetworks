package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import graph.CapGraph;
import graph.Graph;
import util.GraphLoader;

class CapGraphTest {
	private static  Graph testGraph;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// By default load small_test_graph.txt
		loadGraph("data/small_test_graph.txt");
	}

	private static void loadGraph(String fileName) {
		testGraph = new CapGraph();
		GraphLoader.loadGraph(testGraph, fileName);
		System.out.println("Loaded " + fileName + " for CapGraphTest");
		System.out.println(testGraph);
	}

	@Test
	void testNumVertices() {
		loadGraph("data/small_test_graph.txt");
		// Tests for total number of vertices in the graph
		assertEquals(testGraph.exportGraph().size(), 14);
	}

	@Test
	void testEdgeForSampleNode() {
		loadGraph("data/small_test_graph.txt");
		// Tests for edges connected to 8
		Integer nodeToTest = 8;
		Set<Integer> expectedEdges = new HashSet<Integer>(Arrays.asList(7,9,12));
		assertEquals(expectedEdges, testGraph.exportGraph().get(nodeToTest));
	}

	@Test
	void testGetEgonet() {
		// Test on a even smaller dataset
		loadGraph("data/test_data_for_egonet.txt");

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
		System.out.println("Testing egonet on test_data.txt");
		System.out.println("Egonet for 3 - ");
		System.out.println(expectedGraph);

		HashMap<Integer, HashSet<Integer>> expectedEgonet = expectedGraph.exportGraph();
		System.out.println("Expected Egonet - " + expectedEgonet);

		// Get ego net for 3 to test
		HashMap<Integer, HashSet<Integer>> egonetToTest = testGraph.getEgonet(3).exportGraph();
		System.out.println("Egonet under test - " + egonetToTest);

		// Compare expected and received ego net
		for (Map.Entry<Integer, HashSet<Integer>> entry : egonetToTest.entrySet()) {
			Integer node = entry.getKey();
			HashSet<Integer> neighbors = entry.getValue();

			if (expectedEgonet.containsKey(node) &&
					expectedEgonet.get(node).equals(neighbors)){
				continue; 
			}
			else {
				assertTrue(false, "Generated egonet does not match expected ego net");
				break;
			}
		}
		assertTrue(true, "Generated egonet matches exactly with expected ego net");
	}
}
