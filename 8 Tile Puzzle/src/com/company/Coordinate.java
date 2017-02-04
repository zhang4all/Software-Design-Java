package com.company;


/**
 * Coordinate class used to identify the position of the piece in the 2D grid of the board
 */
public class Coordinate {
    public int row;
    public int col;


    /**
     * constructor takes a row and col coordinate and forms a coordinate class
     * @param x row coordinate represents row
     * @param y col coordinate represents column
     */
    public Coordinate(int x, int y) {
        this.row =x;
        this.col =y;
    }
}
