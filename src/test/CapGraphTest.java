package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
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
		String fileName = "data/small_test_graph.txt";
		testGraph = new CapGraph();
		GraphLoader.loadGraph(testGraph, fileName);
		System.out.println("Loaded " + fileName + " for CapGraphTest");
		System.out.println(testGraph);
	}

	@Test
	void testNumVertices() {
		// Tests for total number of vertices in the graph
		assertEquals(testGraph.exportGraph().size(), 14);
	}

	@Test
	void testEdgeForSampleNode() {
		// Tests for edges connected to 8
		Integer nodeToTest = 8;
		Set<Integer> expectedEdges = new HashSet<Integer>(Arrays.asList(7,9,12));
		assertEquals(testGraph.exportGraph().get(nodeToTest), expectedEdges);
	}
}
