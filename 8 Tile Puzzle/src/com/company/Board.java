package com.company;

import java.util.*;


/**
 * Board class is main class the the 8-tiles puzzle
 * Board class contains all the pieces
 */
public class Board {
    public String boardString;
    public int heuristicValue;
    public Coordinate[] numberToCoordinateArrayMap;
    private int[][] boardGrid; // 2d array version
    public String[] possibleMoves;
    public Coordinate[] finishedGridNumberToCoordinateArrayMap;


    /**
     * constructor for option 1 of the game
     * generates a random board
     */
    public Board() {
        // randomly generate a 9 character string with no repeating character
        String randomString = randomStringGenerator();

        boardString = randomString;
        numberToCoordinateArrayMap = new Coordinate[9];
        possibleMoves = new String[9];
        for (int i=0; i<9; i++){
            possibleMoves[i] = "";
        }

        // create the boardGrid and array map
        stringToBoard(boardString);

        // create the finished array map
        finishedGridNumberToCoordinateArrayMap = new Coordinate[9];
        finishedGridNumberToCoordinateArrayMap[0] = new Coordinate(2,2);
        finishedGridNumberToCoordinateArrayMap[1] = new Coordinate(0,0);
        finishedGridNumberToCoordinateArrayMap[2] = new Coordinate(0,1);
        finishedGridNumberToCoordinateArrayMap[3] = new Coordinate(0,2);
        finishedGridNumberToCoordinateArrayMap[4] = new Coordinate(1,0);
        finishedGridNumberToCoordinateArrayMap[5] = new Coordinate(1,1);
        finishedGridNumberToCoordinateArrayMap[6] = new Coordinate(1,2);
        finishedGridNumberToCoordinateArrayMap[7] = new Coordinate(2,0);
        finishedGridNumberToCoordinateArrayMap[8] = new Coordinate(2,1);

        // calculate the heuristic value
        heuristicValue = calculateHeuristicValue();

        generateMovablePieces();
    }


    /**
     * constructor for option 2 of the game with the player setting the starting configuration
     * @param boardInput a string representation of the board
     */
    public Board(String boardInput) {
        boardString = boardInput;
        numberToCoordinateArrayMap = new Coordinate[9];
        possibleMoves = new String[9];
        for (int i=0; i<9; i++){
            possibleMoves[i] = "";
        }

        // create the boardGrid and array map
        stringToBoard(boardString);

        // create the finished array map
        finishedGridNumberToCoordinateArrayMap = new Coordinate[9];
        finishedGridNumberToCoordinateArrayMap[0] = new Coordinate(2,2);
        finishedGridNumberToCoordinateArrayMap[1] = new Coordinate(0,0);
        finishedGridNumberToCoordinateArrayMap[2] = new Coordinate(0,1);
        finishedGridNumberToCoordinateArrayMap[3] = new Coordinate(0,2);
        finishedGridNumberToCoordinateArrayMap[4] = new Coordinate(1,0);
        finishedGridNumberToCoordinateArrayMap[5] = new Coordinate(1,1);
        finishedGridNumberToCoordinateArrayMap[6] = new Coordinate(1,2);
        finishedGridNumberToCoordinateArrayMap[7] = new Coordinate(2,0);
        finishedGridNumberToCoordinateArrayMap[8] = new Coordinate(2,1);

        // calculate the heuristic value
        heuristicValue = calculateHeuristicValue();

        generateMovablePieces();
    }


    /**
     * randomly generates a string from 0 to 8 without repeats
     * stores the random board string in the member variable
     * @return a random board string
     */
    private String randomStringGenerator() {
        Random randomGenerator = new Random();
        randomGenerator.setSeed(System.currentTimeMillis());
        ArrayList<String> pieces = new ArrayList<>();
        int randomValue;
        String cumulativeString = "";

        while (pieces.size() <9){
            randomValue = randomGenerator.nextInt(9);
            String randomString = Integer.toString(randomValue);
            if (!pieces.contains(randomString)) {
                pieces.add(randomString);
            }
        }
        for (int i=0; i<9; i++) {
            cumulativeString = cumulativeString.concat(pieces.get(i));
        }

        return cumulativeString;
    }


    /**
     * converts a board string to a 2D grid of the board
     * @param inputString
     */
    private void stringToBoard(String inputString){
        boardGrid = new int[3][3];

        int stringIndex=0;
        for (int row=0; row<3; row++){
            for (int col=0; col<3; col++){
                char indexChar =inputString.charAt(stringIndex);
                int indexValue = Character.getNumericValue(indexChar);
                boardGrid[row][col] =  indexValue;
                numberToCoordinateArrayMap[indexValue] = new Coordinate(row, col);
                stringIndex++;
            }
        }
    }


    /**
     * converts the board array to a 2D grid
     * @param array an array of string index to coordinates
     * @return 2D grid of the board
     */
    private int[][] arrayToBoard(Coordinate[] array) {
        int[][] board = new int[3][3];

        for (int i=0; i<9; i++) {
            board[array[i].row][array[i].col] = i;
        }

        return board;
    }


    /**
     * converts the 2D grid of the board to a string
     * @param board 2D grid of the board
     * @return the board string
     */
    private String boardToString(int[][] board) {
        String newBoardString = "";

        for (int row=0; row<3; row++){
            for (int col=0; col<3; col++){
                newBoardString = newBoardString + Integer.toString(board[row][col]);
            }
        }

        return newBoardString;
    }


    /**
     *
     * @return the heuristic value of the current board
     */
    private int calculateHeuristicValue() {
        int cumulativeValue=0;
        int gridValue;
        int xDifference;
        int yDifference;

        for (int row=0; row<3; row++) {
            for (int col = 0; col < 3; col++) {
                gridValue = boardGrid[row][col];
                xDifference = Math.abs(finishedGridNumberToCoordinateArrayMap[gridValue].row - row);
                yDifference = Math.abs(finishedGridNumberToCoordinateArrayMap[gridValue].col - col);
                cumulativeValue = cumulativeValue + xDifference + yDifference;
            }
        }

        return cumulativeValue;
    }


    /**
     * prints the 2D board
     */
    public void printBoard() {
        for (int row=0; row<3; row++){
            System.out.printf("   ");
            for (int col=0; col<3; col++) {
                if (boardGrid[row][col] == 0) {
                    System.out.print("  ");
                }
                else {
                    System.out.print(boardGrid[row][col]);
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }


    /**
     *
     * @param arrayMap an array that maps index to coordinate
     * @return the a deep copy of the array of coordinates
     */
    private Coordinate[] copyArrayMap(Coordinate[] arrayMap) {
        Coordinate[] copiedArrayMap = new Coordinate[9];

        for (int i=0; i<9; i++) {
            copiedArrayMap[i] = new Coordinate(arrayMap[i].row, arrayMap[i].col);
        }

        return copiedArrayMap;
    }


    /**
     *
     * @param x1 row of the 0
     * @param y1 column of the 0
     * @param x2 row of the adjacent value
     * @param y2 column of the adjacent value
     * @return the board string of the new board after the switching of pieces
     */
    private String getMovedBoardString(int x1, int y1, int x2, int y2) {
        Coordinate[] copiedArrayMap;

        int adjacentValue = boardGrid[x2][y2];
        copiedArrayMap = copyArrayMap(numberToCoordinateArrayMap);
        copiedArrayMap[adjacentValue] = new Coordinate(x1,y1);
        copiedArrayMap[0] = new Coordinate(x2,y2);
        int[][] board = arrayToBoard(copiedArrayMap);

        return boardToString(board);
    }


    /**
     * updates the possibleMoves array with new board strings
     */
    public void generateMovablePieces() {
        int rowOfZero = numberToCoordinateArrayMap[0].row;
        int colOfZero = numberToCoordinateArrayMap[0].col;

        if ((rowOfZero == 0) && (colOfZero==0)) {
            possibleMoves[boardGrid[0][1]] = getMovedBoardString(0,0,0,1);
            possibleMoves[boardGrid[1][0]] = getMovedBoardString(0,0,1,0);
        }
        else if ((rowOfZero == 0) && (colOfZero==1)) {
            possibleMoves[boardGrid[0][0]] = getMovedBoardString(0,1,0,0);
            possibleMoves[boardGrid[0][2]] = getMovedBoardString(0,1,0,2);
            possibleMoves[boardGrid[1][1]] = getMovedBoardString(0,1,1,1);
        }
        else if ((rowOfZero == 0) && (colOfZero==2)) {
            possibleMoves[boardGrid[0][1]] = getMovedBoardString(0,2,0,1);
            possibleMoves[boardGrid[1][2]] = getMovedBoardString(0,2,1,2);
        }
        else if ((rowOfZero == 1) && (colOfZero==0)) {
            possibleMoves[boardGrid[0][0]] = getMovedBoardString(1,0,0,0);
            possibleMoves[boardGrid[1][1]] = getMovedBoardString(1,0,1,1);
            possibleMoves[boardGrid[2][0]] = getMovedBoardString(1,0,2,0);
        }
        else if ((rowOfZero == 1) && (colOfZero==1)) {
            possibleMoves[boardGrid[0][1]] = getMovedBoardString(1,1,0,1);
            possibleMoves[boardGrid[1][0]] = getMovedBoardString(1,1,1,0);
            possibleMoves[boardGrid[1][2]] = getMovedBoardString(1,1,1,2);
            possibleMoves[boardGrid[2][1]] = getMovedBoardString(1,1,2,1);
        }
        else if ((rowOfZero == 1) && (colOfZero==2)) {
            possibleMoves[boardGrid[0][2]] = getMovedBoardString(1,2,0,2);
            possibleMoves[boardGrid[1][1]] = getMovedBoardString(1,2,1,1);
            possibleMoves[boardGrid[2][2]] = getMovedBoardString(1,2,2,2);
        }
        else if ((rowOfZero == 2) && (colOfZero==0)) {
            possibleMoves[boardGrid[1][0]] = getMovedBoardString(2,0,1,0);
            possibleMoves[boardGrid[2][1]] = getMovedBoardString(2,0,2,1);
        }
        else if ((rowOfZero == 2) && (colOfZero==1)) {
            possibleMoves[boardGrid[2][0]] = getMovedBoardString(2,1,2,0);
            possibleMoves[boardGrid[1][1]] = getMovedBoardString(2,1,1,1);
            possibleMoves[boardGrid[2][2]] = getMovedBoardString(2,1,2,2);
        }
        else {
            possibleMoves[boardGrid[1][2]] = getMovedBoardString(2,2,1,2);
            possibleMoves[boardGrid[2][1]] = getMovedBoardString(2,2,2,1);
        }
    }
}
