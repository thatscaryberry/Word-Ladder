import java.io.*;
import java.util.*;

public class Graph<K, V> {
    private Map<K, Set<K>> map = new LinkedHashMap<>();
    // adds a node to the graph and checks for duplicates.
    public boolean addNode(K name, V data) {
    if (map.containsKey(name)) {
        return false;
    }
    map.put(name, new HashSet<>());
    return true;
}

// adds a list of nodes to the graph and checks for duplicates.
// You may throw an exception if the length of the names and data arrays are different.
public boolean addNodes(K[] names, V[] data) throws Exception {
    if (names.length != data.length) {
        throw new Exception("Unequal length for the names and data array");
    }
    for (int count = 0; count < names.length; count++) {
        if (!map.containsKey(names[count])) {
            map.put(names[count], new HashSet<>());
        }
    }
    return true;
}

// adds an undirected edge from node 'from' to node 'to'
public boolean addEdge(K from, K to) {
    if (!map.containsKey(from)) {
        return false;
    }
    if (!map.containsKey(to)) {
        return false;
    }
    map.get(from).add(to);
    map.get(to).add(from);
    return true;
}

public String getNodeFromFile(int nodeId, String filename) {

    //read the file and return the node name
    try {
        Scanner scanner = new Scanner(new FileReader(filename));
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" ");
            int id = Integer.parseInt(line[0]);
            if (id == nodeId){
                return line[1];
            }
        }
        scanner.close();
    } catch (FileNotFoundException e) {
      
        e.printStackTrace();
    }

    return "";
    

    
}
public Set<K> getNodeData(K name) {
    return map.get(name);
}

// adds an undirected edge from node from to each node in toList
public boolean addEdge(K from, K[] toList) {
    //add a bunch of edges from one node to a list of nodes
    for (int count = 0; count < toList.length; count++) {
        addEdge(from, toList[count]);
    }
    return true;
}

// removes a node from the graph along with all connected edges
public boolean removeNode(K name) {
    if (!map.containsKey(name)) {
        return false;
    }
    // remove all edges to the node with name
    for (K neighbor : map.get(name)) {
        map.get(neighbor).remove(name);
    }
    map.remove(name);
    return true;
}

// removes each node in nodelist and their edges from the graph
public boolean removeNodes(K[] nodelist) {
    for (int count = 0; count < nodelist.length; count++) {
        removeNode(nodelist[count]);
    }
    return true;
}
    /*prints the graph in the same adgacency list format in the 'read' method
     * described below. 
     */
    public void printGraph(K[] path, String filename){
        System.out.print("[");
        for (int i = 0; i < path.length; i++) {
            
            int p = (int) path[i];
            System.out.print(getNodeFromFile(p, filename));
            
            if (i < path.length - 1) {
                System.out.print(" -> ");
            }
            
        }
        System.out.print("]\n");

    }
    /*constructs a graph from a text file using the following format:
    <nodename1> <neighbor1> <neighbor2> ...
    <nodename2> <neighbor1> <neighbor2> ...*/
    public static <V> Graph<String,V> read(String filename){
        Graph<String, V> graph = new Graph<String, V>();

        BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
            
            //runs line by line adding to the HashMap
			while (line != null) {
                String[] parts = line.split(" ");
                String name = parts[0];
                String[] toList = new String[parts.length - 1];
                //adds the neighbors to the list
                for(int count = 1; count < parts.length; count++){
                    toList[count - 1] = parts[count];
                }
                
                graph.addEdge(name, toList);

				line = reader.readLine();
			}

			reader.close();
            
            
		} catch (IOException e) {
			e.printStackTrace();
		}
        return graph;
    

    }
        /*returns the result, i.e. the path or a list of node names, of depth-first search between
    nodes from and to. It should return an empty array if no path exists. The first entry of the returned array
    should be from and the last value of the array should be to. Each consecutive value should be the name of a
    node that can be reached by a single edge from the previous value in the array.*/

    public K[] DFS(K from, K to) {
        //stored in stack
        Stack<K> stack = new Stack<>();
        Map<K, K> parent = new HashMap<>();
        stack.push(from);
        parent.put(from, null);
        //stores neighbors in stack
        while (!stack.isEmpty()) {
            K current = stack.pop();
            if (current.equals(to)) {
                break;
            }
            //adds neighbors to stack
            for (K neighbor : map.get(current)) {
                if (!parent.containsKey(neighbor)) {
                    stack.push(neighbor);
                    parent.put(neighbor, current);
                }
            }
        }
        //creates list of paths
        List<K> path = new ArrayList<>();
        K current = to;
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path.toArray((K[]) new Integer[path.size()]);
    }
    
    public K[] BFS(K from, K to) {
        Queue<K> queue = new LinkedList<>();
        Map<K, K> parent = new HashMap<>();
        queue.add(from);
        parent.put(from, null);
        //stores the neighbors in queue
        while (!queue.isEmpty()) {
            K current = queue.remove();
            if (current.equals(to)) {
                break;
            }
            //adds to queue
            for (K neighbor : map.get(current)) {
                if (!parent.containsKey(neighbor)) {
                    queue.add(neighbor);
                    parent.put(neighbor, current);
                }
            }
        }
        //creates list of paths
        List<K> path = new ArrayList<>();
        K current = to;
        //adds to list
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path.toArray((K[]) new Integer[path.size()]);
    }
}


