/**
 * @author UCSD MOOC development team
 * 
 * Utility class to add vertices and edges to a graph
 *
 */
package util;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

public class GraphLoaderWithLabels {
    /**
     * Loads graph with data from a file.
     * The file should consist of lines with 4 columns each, corresponding
     * to source_id (int),source_label(string),target_id(int),target_label(string)
     */ 
    public static void loadGraph(graph.SocialNetworkGraph g, String filename){
        try {
            CSVReader reader = new CSVReader(new FileReader(filename));
            for (Iterator<String[]> it = reader.iterator(); it.hasNext(); ) {
                String[] row = it.next();

                Integer vertex1Id;
                Integer vertex2Id;
                try {
                    vertex1Id = Integer.parseInt(row[0]);
                    vertex2Id = Integer.parseInt(row[2]);
                } catch(Exception e){
                    continue;
                }

                String vertex1Label = row[1];
                String vertex2Label = row[3];

                if (vertex1Label.contains("name")
                        || vertex1Label.contains("source")
                        || vertex1Label.contains("target ")
                        || vertex2Label.contains("name")
                        || vertex2Label.contains("source")
                        || vertex2Label.contains("target ")
                ) continue;

                g.addVertex(vertex1Id, vertex1Label);
                g.addVertex(vertex2Id, vertex2Label);
                g.addEdge(vertex1Id, vertex2Id);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}