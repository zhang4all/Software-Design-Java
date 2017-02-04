package com.company;

import java.util.Comparator;


/**
 * Compare object used for the priority queue to implement a min heap of heuristic values
 */
public class CompareHeuristicValue implements Comparator<Node> {
    /**
     *
     * @param first first node
     * @param second second node
     * @return -1 if first < second, 1 if first > second, 0 if equal
     */
    public int compare(Node first, Node second) {
        if (first.board.heuristicValue < second.board.heuristicValue) {
            return -1;
        }
        if (first.board.heuristicValue > second.board.heuristicValue) {
            return 1;
        }
        return 0;
    }
}
