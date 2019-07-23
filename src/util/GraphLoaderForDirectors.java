/**
 * @author UCSD MOOC development team
 * 
 * Utility class to add vertices and edges to a graph
 *
 */
package util;

import com.opencsv.CSVReader;
import graph.SocialNetworkGraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class GraphLoaderForDirectors {
    /**
     * Loads graph with data from a file.
     * The file should consist of lines with 4 columns each, corresponding
     * to source_id (int),source_label(string),target_id(int),target_label(string)
     */ 
    public static void loadGraph(graph.SocialNetworkGraph g, String filename){
        Map<Integer, String> directorIdToNameMap = new HashMap<>();
        Map<String, List<Integer>> actorToDirectorIdsMap = new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filename));
            for (Iterator<String[]> it = reader.iterator(); it.hasNext(); ) {
                String[] row = it.next();

                Integer directorId;

                try {
                    directorId = Integer.parseInt(row[0]);
                } catch(Exception e){
                    continue;
                }

                String directorName = row[1];
                String actorName = row[2];

                if (directorName.contains("name")
                        || actorName.contains("name")
                ) continue;
                directorIdToNameMap.put(directorId, directorName);
                if (!actorToDirectorIdsMap.containsKey(actorName)){
                    actorToDirectorIdsMap.put(actorName, new ArrayList<>());
                }
                actorToDirectorIdsMap.get(actorName).add(directorId);

                //g.addEdge(directorId, vertex2Id);
            }

            /*for(Map.Entry<String, List<Integer>> entry: actorToDirectorIdsMap.entrySet()){
                System.out.print(entry.getKey() + " -> ");
                entry.getValue().forEach(id -> System.out.print(directorIdToNameMap.get(id) + ", "));
                System.out.println();
            }*/

            for(List<Integer> directors: actorToDirectorIdsMap.values()){
                Integer vertex1Id = directors.get(0);
                String vertex1Label = directorIdToNameMap.get(vertex1Id);

                for(Integer director: directors.subList(1, directors.size())){
                    Integer vertex2Id = director;
                    String vertex2Label = directorIdToNameMap.get(director);

                    g.addVertex(vertex1Id, vertex1Label);
                    g.addVertex(vertex2Id, vertex2Label);
                    g.addEdge(vertex1Id, vertex2Id);
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SocialNetworkGraph testGraph = new SocialNetworkGraph();

        loadGraph(testGraph, "data/directors.csv");

    }
}