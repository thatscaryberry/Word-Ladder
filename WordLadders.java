import java.io.*;
import java.util.*;

public class WordLadders {
    // Hashtable to store word to node id mappings
    private static Hashtable<String, Integer> wordToNode = new Hashtable<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();

        // Read input file and construct graph
        Graph<Integer, String> graph = readWordGraph(filename);
        System.out.println("Graph constructed.");

        // Loop until user quits
        while (true) {
            System.out.print("Enter start word (or 'quit' to exit): ");
            String startWord = scanner.nextLine().toLowerCase();
            if (startWord.equals("quit")) {
                break;
            }

            System.out.print("Enter end word: ");
            String endWord = scanner.nextLine().toLowerCase();

            // Check if both words are in the graph
            if (!wordToNode.containsKey(startWord) || !wordToNode.containsKey(endWord)) {
                System.out.println("One or both words not found in graph.");
                continue;
            }

            int startNode = wordToNode.get(startWord);
            int endNode = wordToNode.get(endWord);

            System.out.print("Enter search type (BFS or DFS): ");
            String searchType = scanner.nextLine().toUpperCase();
            Integer[] path;
            
            // Perform BFS or DFS search on the graph
            if (searchType.equals("BFS")) {
                path = graph.BFS(startNode, endNode);
            } else if (searchType.equals("DFS")) {
                path = graph.DFS(startNode, endNode);
            } else {
                System.out.println("Invalid search type.");
                continue;
            }

            // Print the path between the start and end words
            if (path == null) {
                System.out.println("No path found between " + startWord + " and " + endWord);
            } else {
                System.out.println("Path found between " + startWord + " and " + endWord + ":");
                graph.printGraph(path, filename);
            }
        }

        scanner.close();
    }

    // Read input file and construct graph
    public static Graph<Integer, String> readWordGraph(String filename) {
        Graph<Integer, String> graph = new Graph<>();
        
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
        
            int nodeCount = 0;
        
            // Loop through each line of the input file
            while (scanner.hasNextLine()) {
                // Parse the line to get node id, node data and neighbor ids
                String[] line = scanner.nextLine().split(" ");
                int nodeId = Integer.parseInt(line[0]);
                String nodeData = line[1];
        
                // Add node data to the hashtable
                if (!wordToNode.containsKey(nodeData)) {
                    wordToNode.put(nodeData, nodeId);
                }
        
                // Add node to the graph
                graph.addNode(nodeId, nodeData);
        
                // Add edges to neighbors
                for (int i = 2; i < line.length; i++) {
                    int neighborId = Integer.parseInt(line[i]);
                    graph.addEdge(nodeId, neighborId);
                }
        
                nodeCount++;
            }
        
            System.out.println("Read " + nodeCount + " nodes from file.");
        
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
        return graph;
    }
}
