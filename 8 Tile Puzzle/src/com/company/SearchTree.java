package com.company;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;


/**
 * class that contains the data structures and assists with automatically solving the puzzle
 */
public class SearchTree {
    private PriorityQueue<Node> pq;
    private HashMap stringToNodeMap;
    public Node startNode;
    public Node closestNode;


    /**
     * constructor for the search tree
     * @param startNode sets the start node of the search tree
     */
    public SearchTree(Node startNode){
        this.startNode = startNode;

        // create priority queue and add start node to it
        Comparator<Node> comparer = new CompareHeuristicValue();

        pq = new PriorityQueue(181442, comparer);
        pq.add(startNode);

        // create hash map and add start node to it
        stringToNodeMap = new HashMap();
        stringToNodeMap.put(startNode.board.boardString, startNode);

        // sets the start node as the closest node
        closestNode = startNode;
    }


    /**
     * adds the node to the min heap if it hasn't previously been added
     * creates the new board and sets the previous node for the search tree
     * updates the node with the lowest heuristic value as closestNode
     * @param parentNode used to set the previous node of the child node
     * @param childBoardString used to create the new board object
     */
    public void addNode(Node parentNode, String childBoardString) {
        // check if node is in map
        if (!stringToNodeMap.containsKey(childBoardString)){
            // create the board
            Board b = new Board(childBoardString);
            // create the node
            Node nodeBeingAdded = new Node(b);

            // updates closest board
            if (nodeBeingAdded.board.heuristicValue < closestNode.board.heuristicValue){
                closestNode = nodeBeingAdded;
            }

            // adds the parent and child links
            nodeBeingAdded.setPreviousNode(parentNode);

            pq.add(nodeBeingAdded);
            stringToNodeMap.put(childBoardString, nodeBeingAdded);
        }
    }


    /**
     *
     * @return true if the min heap is not empty
     */
    public boolean isPQNotEmpty() {
        return (pq.size() != 0);
    }


    /**
     *
     * @return the Node with the smallest heuristic value
     */
    public Node getNextNode() {
        return pq.poll();
    }
}
