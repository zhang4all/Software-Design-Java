package com.company;


/**
 * node class contains a board along with the next and previous nodes for the search tree
 */
public class Node {
    public Board board;
    public Node nextNode;
    public Node previousNode;
    public int totalNodes;


    /**
     * constructor for the node
     * @param currentBoard takes a board as the input
     */
    public Node(Board currentBoard) {
        board = currentBoard;
    }


    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }


    public Node getPreviousNode() {
        return previousNode;
    }


    public void setNextNode(Node nextNode){
        this.nextNode = nextNode;
    }


    public Node getNextNode() {
        return nextNode;
    }
}
