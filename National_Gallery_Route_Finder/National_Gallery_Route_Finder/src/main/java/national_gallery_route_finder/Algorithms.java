package national_gallery_route_finder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Algorithms {

        public static <T> List<List<Node<?>>> findAllPathsDepthFirst(Node<?> start, List<Node<?>> encountered, T end) {
            List<List<Node<?>>> result = null, temp2;
            if (start.data.equals(end)) {                                       //Found end.
                List<Node<?>> temp = new ArrayList<>();                         //Create a solution path list and-
                temp.add(start);                                                //-add the start node.
                result = new ArrayList<>();                                     //Create a list of resulting permutations and-
                result.add(temp);                                               //-add the path list.
                return result;                                                  //Return the list of permutations.
            }
            if (encountered == null) encountered = new ArrayList<>();           //Create an empty list of encountered nodes.
            if (!encountered.contains(start)) {
                encountered.add(start);                                         //Add the start node to encountered list
                for (Link adjNode : start.adjList) {                            //Cycle through the adjacent nodes.
                    if (!encountered.contains(adjNode)) {                       //If the encountered node is not adjacent;
                        //Call findAllPathsDepthFirst() recursively.
                        temp2 = findAllPathsDepthFirst(adjNode.destNode, new ArrayList<>(encountered), end);     //Duplicate the encountered list.
                        if (temp2 != null) {                                    //If the result contains one or more paths to the solution node-
                            for (List<Node<?>> x : temp2)                       //-for each partial path list returned-
                                x.add(0, start);                          //-add the current node to the start of each path list.
                            if (result == null)
                                result = temp2;                                 //If this is the first set of solution paths found, use it as the result.
                            else result.addAll(temp2);                          //Otherwise, append them to the previously found paths.
                        }
                    }
                }
            }
            return result;
        }

        public static <T> Cost searchGraphDepthFirstCheapestPath(Node<?> start, List<Node<?>> encountered, int totalCost, T end) {
            if (start.data.equals(end)) {                                       //Found end.
                Cost pathCost = new Cost();                                     //Create a cost object and-
                pathCost.pathList.add(start);                                   //-add the current node to it.
                pathCost.pathCost = totalCost;                                  //Use the current total cost.
                return pathCost;                                                //Return the CostedPath object.
            }
            if (encountered == null) encountered = new ArrayList<>();           //Create an empty list of encountered nodes.
            encountered.add(start);                                             //Add the start node.
            List<Cost> allPaths = new ArrayList<>();                            //List of all potential paths.
            for (Link adjLink : start.adjList)
                if (!encountered.contains(adjLink.destNode)) {                  //For each adjacent node not yet encountered-
                    Cost temp = searchGraphDepthFirstCheapestPath(adjLink.destNode, new ArrayList<>(encountered),
                            totalCost + adjLink.cost, end);             //-create a new Cost. start this node to the searched for item (if a valid path exists).
                    if (temp == null) continue;                                 //No path was found, so continue to the next iteration
                    temp.pathList.add(0, start);                          //Add the current node to the front of the path list
                    allPaths.add(temp);                                         //Add the new candidate path to the list of all costed paths
                }                                                               //If no paths were found then return null. Otherwise, return the cheapest path (i.e. the one with min pathCost)
            return allPaths.isEmpty() ? null : Collections.min(allPaths, Comparator.comparingInt(p -> p.pathCost));
        }


        public static <T> Cost findCheapestPathDijkstra(Node<?> startNode, T end) {
            Cost costPath = new Cost();                                                         //Create a Cost object..
            List<Node<?>> encountered = new ArrayList<>(), notEncountered = new ArrayList<>();  //Create lists for encountered and notEncountered nodes.
            startNode.nodeValue = 0;                                                            //Set the starting node value to zero.
            notEncountered.add(startNode);                                                      //Add the start node to the notEncountered list.
            Node<?> currentNode;
            do {                                                                                //Loop until notEncountered list is empty.
                currentNode = notEncountered.remove(0);                                   //Get the first notEncountered node (sorted list, lowest value).
                encountered.add(currentNode);                                                   //Record current node in encountered list.
                if (currentNode.data.equals(end)) {                                             //If the end node is found - assemble path list back to start and return it.
                    costPath.pathList.add(currentNode);                                         //Add the current (goal) node to the result list (only element).
                    costPath.pathCost = currentNode.nodeValue;                                  //The total cheapest path cost is the node value of the current/goal node.
                    while (currentNode != startNode) {                                          //While we're not back to the start node...
                        boolean foundPrevPathNode = false;                                      //Use a flag to identify when the previous path node is identified.
                        for (Node<?> n : encountered) {                                         //For each node in the encountered list...
                            for (Link e : n.adjList)                                            //For each edge from that node...
                                if (e.destNode == currentNode && currentNode.nodeValue - e.cost == n.nodeValue) { //If that edge links to the-
                                    //-current node and the difference in node values is the cost of the edge -> found path node!
                                    costPath.pathList.add(0, n);                          //Add the identified path node to the front of the result list.
                                    currentNode = n;                                            //Move the currentNode reference back to the identified path node.
                                    foundPrevPathNode = true;                                   //Set the flag to break the outer loop.
                                    break;
                                }
                            if (foundPrevPathNode)
                                break;                                                          //We've identified the previous path node, so break the inner loop to continue.
                        }
                    }
                    for (Node<?> n : encountered) n.nodeValue = Integer.MAX_VALUE;
                    for (Node<?> n : notEncountered) n.nodeValue = Integer.MAX_VALUE;
                    return costPath;                                                            //The costed (cheapest) path has been assembled, so return it!
                }
                for (Link e : currentNode.adjList)                                              //For each edge/link from the current node...
                    if (!encountered.contains(e.destNode)) {                                    //If the node it leads to has not yet been encountered (i.e. processed).
                        e.destNode.nodeValue = Integer.min(e.destNode.nodeValue, currentNode.nodeValue + e.cost); //Update the node value at the end-
                        //-of the edge to the minimum of its current value or the total of the current node's value plus the cost of the edge.
                        notEncountered.add(e.destNode);
                    }
                notEncountered.sort(Comparator.comparingInt(n -> n.nodeValue));                 //Sort in ascending node value order.
            } while (!notEncountered.isEmpty());
            return null;                                                                        //No path found, so return null.
        }
    }


