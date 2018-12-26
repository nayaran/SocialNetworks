package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

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


	@Test
	void testDfs() {
		// Test on a even smaller dataset
		System.out.println("\nTEST - testDfs");
		CapGraph testGraphForDfs;
		Stack<Integer> expectedStack;
		Stack<Integer> stackToTest;

		// Initialize
		testGraphForDfs = new CapGraph();
		loadGraph(testGraphForDfs, "data/test_data_for_scc.txt");

		expectedStack = new Stack<Integer>();
		ArrayList<Integer> testData = new ArrayList<Integer>(Arrays.asList(50, 44, 18, 23, 65, 25, 32));
		for (Integer vertex: testData) {
			expectedStack.push(vertex);
		}

		Stack<Integer> vertices = new Stack<Integer>();
		for (Integer vertex: testGraphForDfs.exportGraph().keySet()){
			vertices.push(vertex);
		}
		stackToTest = testGraphForDfs.DFS(vertices, testGraphForDfs, new ArrayList<Stack<Integer>>());
		System.out.println("Expected stack - " + expectedStack);
		System.out.println("Stack to test - " + stackToTest);
		assertEquals(expectedStack, stackToTest);
	}

	@Test
	void testGetSCC() {
		System.out.println("\nTEST - testGetSCC");
		// Test on a even smaller dataset
		loadGraph(testGraph, "data/test_data_for_scc.txt");

		List<Graph> expectedListOfSCC = new ArrayList<Graph>();
		Graph firstSCC = new CapGraph();
		firstSCC.addVertex(32);
		Graph secondSCC = new CapGraph();
		secondSCC.addVertex(44);
		Graph thirdSCC = new CapGraph();
		thirdSCC.addVertex(50);
		Graph fourthSCC = new CapGraph();
		fourthSCC.addVertex(25);
		fourthSCC.addVertex(65);
		fourthSCC.addVertex(23);
		fourthSCC.addVertex(18);
		fourthSCC.addEdge(25,  23);
		fourthSCC.addEdge(25,  65);
		fourthSCC.addEdge(25,  18);
		fourthSCC.addEdge(65,  23);
		fourthSCC.addEdge(18,  23);
		fourthSCC.addEdge(23,  25);
		fourthSCC.addEdge(23,  18);
		expectedListOfSCC.add(firstSCC);
		expectedListOfSCC.add(secondSCC);
		expectedListOfSCC.add(thirdSCC);
		expectedListOfSCC.add(fourthSCC);

		List<Graph> listOfSCCToTest = testGraph.getSCCs();
		System.out.println("Expected list of SCC - " + expectedListOfSCC);
		System.out.println("List of SCC to test- " + listOfSCCToTest);
		boolean result = compareListOfGraphs(expectedListOfSCC, listOfSCCToTest);

		if (result == false) {
			assertFalse(true, "Generated list of SCC do not match expected list of SCC");
		}
	}

	@Test
	void testTransposeGraph() {
		System.out.println("\nTEST - testTransposeGraph");
		loadGraph(testGraph, "data/test_data_for_egonet.txt");
		Graph expectedGraph;
		Graph graphToTest;

		//Initialize
		expectedGraph = new CapGraph();
		loadGraph(expectedGraph, "data/test_data_for_egonet_transpose.txt");
		graphToTest = ((CapGraph) testGraph).getTransposedGraph();

		System.out.println("Expected graph - " + expectedGraph);
		System.out.println("Graph to test - " + graphToTest);
		assertEquals(expectedGraph, graphToTest);
	}

	private boolean compareListOfGraphs(List<Graph> firstList, List<Graph> secondList) {
		if (firstList.size() != secondList.size()) {
			return false;
		}
		for (Graph graph: firstList) {
			if (!secondList.contains(graph))
			{
				return false;
			}
		}
		return true;
	}
}
