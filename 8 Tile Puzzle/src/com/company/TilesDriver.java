/**
 * Program 4: 8 Tiles GUI
 *
 * Class: CS 342, Fall 2016
 * System Windows, IntelliJ IDE
 * Author Code Number: host
 *
 */


package com.company;


import java.lang.String;
import java.util.Scanner;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.stage.*;
import javafx.util.Duration;


public class TilesDriver extends Application {
    int grid[][] = {{0,0,0}, {0,0,0}, {0,0,0}};
    static double mousePositionX;
    static double mousePositionY;
    int setBoardClickNumber=0;
    static String newBoardString;
    static Node currentNode;
    static Node solveStartNode;
    static Board mainBoard;
    static int totalNodes;
    Line line1;
    Line line2;
    Line line3;
    Line line4;
    Line line5;
    Line line6;
    Line line7;
    Button setBoardBtn;
    Button solveBtn;
    Button exitBtn;
    Group buttons;
    Scene scene;
    Label mousePosition;
    static int nodeCount;


    public static void starterText() {
        System.out.println("Class: CS 342, Fall 2016");
        System.out.println("Program: #4, 8 Tiles GUI.");
        System.out.println();
        System.out.println();
        System.out.println("Welcome to the 8-tiles puzzle. ");
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
     * starts from the finished board and works back to the start node
     * sets the next node for all previous nodes
     * @param startNode this is the first board that was passed into the solver
     * @param endNode this is the finished board
     */
    public static void arrangeNodeOrder(Node startNode, Node endNode) {
        totalNodes=1;
        Node currentNode = endNode;
        Node previousNode = currentNode.getPreviousNode();
        previousNode.setNextNode(currentNode);
        totalNodes++;

        while (previousNode != startNode){
            currentNode = previousNode;
            previousNode = currentNode.getPreviousNode();
            previousNode.setNextNode(currentNode);
            totalNodes++;
        }
    }


    public static void printSolvedPuzzle(Node startNode, Node endNode) {
        Node currentNode = startNode;
        int i=1;

        while (currentNode != endNode) {
            System.out.format("%d.%n", i);
            currentNode.board.printBoard();
            System.out.println("Heuristic value: " + currentNode.board.heuristicValue);
            System.out.println();
            currentNode = currentNode.getNextNode();
            i++;
        }

        //prints out last board
        System.out.format("%d.%n", i);
        currentNode.board.printBoard();
        System.out.println("Heuristic value: " + currentNode.board.heuristicValue);
        System.out.println();
        System.out.println("Done.");
    }


    /**
     * automatically solves the puzzle from the input board
     * @param b the board position right before being passed into the solver
     */
    public static void solve(Board b) {
        currentNode = new Node(b);
        solveStartNode = currentNode;

        SearchTree solverTree = new SearchTree(currentNode);

        System.out.println("Solving puzzle automatically...........................");

        while (solverTree.isPQNotEmpty()) {
            if (currentNode.board.heuristicValue == 0) {
                // pass the end node into a function that prints out all boards
                arrangeNodeOrder(solverTree.startNode, currentNode);
                printSolvedPuzzle(solveStartNode, currentNode);
                return;
            }

            // get list of children nodes
            currentNode.board.generateMovablePieces();

            // adds new children to search tree
            for (String childBoardString: currentNode.board.possibleMoves) {
                if (childBoardString.length() != 0)
                    solverTree.addNode(currentNode, childBoardString);
            }

            // gets node with minimum heuristic value
            currentNode = solverTree.getNextNode();
        }

        System.out.println();
        System.out.println();
        System.out.println("All 181442 moves have been tried.");
        System.out.println("That puzzle is impossible to solve. Best board found was:");
        solverTree.closestNode.board.printBoard();
        System.out.format("Heuristic value: %d %n", solverTree.closestNode.board.heuristicValue);
        System.out.println();
        System.out.println("Exiting program.");
    }


    /**
     * the main loop for manually solving a puzzle
     * @param b the initial board position
     * @param s the scanner object to scan for inputs
     */
    public static void mainLoop(Board b, Scanner s) {
        //newBoardString;
        String moveString;
        int moveInt;
        int i=1;

        // this outer loop moves through different boards
        while (true) {
            System.out.printf(i + ".");
            System.out.println();
            b.printBoard();
            System.out.println("Heuristic value: " + b.heuristicValue);
            System.out.println();
            if (b.heuristicValue == 0) {
                System.out.println("Done.");
                break;
            }
            System.out.print("Piece to move: ");
            moveString = s.next();
            // this inner loop is to check if input is valid
            while (true) {
                if (moveString.compareTo("s") == 0){
                    solve(b);
                    return;
                }
                System.out.println();
                System.out.println();
                moveInt = Integer.parseInt(moveString);
                // following checks if move is on board
                if ((moveInt >=9) || (moveInt <0)){
                    System.out.println("*** Invalid move. Please retry.");
                    System.out.print("Piece to move: ");
                    moveString = s.next();
                    System.out.println();
                    System.out.println();
                    continue;
                }
                newBoardString = b.possibleMoves[moveInt];
                // following checks that move is legal
                if (newBoardString.length() != 9) {
                    System.out.println("*** Invalid move. Please retry.");
                    System.out.print("Piece to move: ");
                    moveString = s.next();
                    System.out.println();
                    System.out.println();
                    continue;
                }
                break;
            }
            b = new Board(newBoardString);
            i++;
        }
    }


    /**
     * initializes the size and location of the lines for the board
     */
    void createLines() {
        line1 = new Line();
        line1.setStartX(0);
        line1.setStartY(0);
        line1.setEndX(240);
        line1.setEndY(0);

        line2 = new Line();
        line2.setStartX(0);
        line2.setStartY(80);
        line2.setEndX(240);
        line2.setEndY(80);

        line3 = new Line();
        line3.setStartX(0);
        line3.setStartY(160);
        line3.setEndX(240);
        line3.setEndY(160);

        line4 = new Line();
        line4.setStartX(0);
        line4.setStartY(240);
        line4.setEndX(240);
        line4.setEndY(240);

        line5 = new Line();
        line5.setStartX(80);
        line5.setStartY(0);
        line5.setEndX(80);
        line5.setEndY(240);

        line6 = new Line();
        line6.setStartX(160);
        line6.setStartY(0);
        line6.setEndX(160);
        line6.setEndY(240);

        line7 = new Line();
        line7.setStartX(240);
        line7.setStartY(0);
        line7.setEndX(240);
        line7.setEndY(240);
    }


    /**
     * initializes the size and location of the set board button
     */
    void createSetBoardBtn() {
        setBoardBtn = new Button("Set Board");
        setBoardBtn.setMinWidth(80);
        setBoardBtn.setMinHeight(40);
        setBoardBtn.setLayoutX(400);
        setBoardBtn.setLayoutY(20);
    }


    /**
     * initializes the size and location of the solve board button
     */
    void createSolveBoardBtn() {
        solveBtn = new Button("Solve");
        solveBtn.setMinWidth(80);
        solveBtn.setMinHeight(40);
        solveBtn.setLayoutX(400);
        solveBtn.setLayoutY(70);
    }


    /**
     * initializes the size and location of the exit board button
     */
    void createExitBoardBtn() {
        exitBtn = new Button("Exit");
        exitBtn.setMinWidth(80);
        exitBtn.setMinHeight(40);
        exitBtn.setLayoutX(400);
        exitBtn.setLayoutY(120);
    }


    /**
     * initializes the size and position of all 9 tile buttons
     */
    void createTileButtons() {
        buttons = new Group();
        for (int i = 0; i < 9; i++) {
            Button btn = new Button(Integer.toString(i));
            btn.setMinWidth(80);
            btn.setMinHeight(80);
            buttons.getChildren().add(btn); // adds individual buttons to buttons group
            // set to be off the screen initially
            btn.setLayoutX(1000);
            btn.setLayoutY(1000);
        }
    }


    /**
     * initializes the location and size of the mouse position label
     */
    void createMousePositionLabel() {
        mousePosition = new Label("here");
        mousePosition.setMinWidth(800);
        mousePosition.setLayoutX(0);
        mousePosition.setLayoutY(500);
    }


    /**
     * prints out the location of the mouse and also prints out the number of mouse clicks done so far
     * @param event
     */
    void mouseMovedHandler(MouseEvent event) {
        if (setBoardClickNumber<10) {
            mousePositionX = event.getSceneX();
            mousePositionY = event.getSceneY();
            String label = "x coordinate: ";
            String newLabel = label.concat(Double.toString(mousePositionX));
            label = newLabel.concat("    y coordinate: ");
            newLabel = label.concat(Double.toString(mousePositionY));
            label = newLabel.concat("    " + "setBoardClickNumber: " + setBoardClickNumber);
            System.out.println(label);
            mousePosition.setText(label);
            if (setBoardClickNumber == 9) {
                // convert it into string
                String newString = boardToString(grid);
                System.out.println(newString);
                mainBoard = new Board(newString);
                mainBoard.printBoard();

                buttons.getChildren().get(0).setLayoutX(1000);
                buttons.getChildren().get(0).setLayoutY(1000);
                setBoardClickNumber++;
            }
        }
    }


    /**
     * button handler for setting up the board by clicks
     */
    void setButtonsWithClicksHandler() {
        if (setBoardClickNumber < 9) {
            if ((mousePositionX < 80) && (mousePositionY < 80)) {
                buttons.getChildren().get(setBoardClickNumber).setLayoutX(0);
                buttons.getChildren().get(setBoardClickNumber).setLayoutY(0);
                grid[0][0] = setBoardClickNumber;
                setBoardClickNumber++;
            } else if ((mousePositionX > 80) && (mousePositionX < 160) && (mousePositionY < 80)) {
                buttons.getChildren().get(setBoardClickNumber).setLayoutX(80);
                buttons.getChildren().get(setBoardClickNumber).setLayoutY(0);
                grid[0][1] = setBoardClickNumber;
                setBoardClickNumber++;
            } else if ((mousePositionX > 160) && (mousePositionX < 240) && (mousePositionY < 80)) {
                buttons.getChildren().get(setBoardClickNumber).setLayoutX(160);
                buttons.getChildren().get(setBoardClickNumber).setLayoutY(0);
                grid[0][2] = setBoardClickNumber;
                setBoardClickNumber++;
            } else if ((mousePositionX > 0) && (mousePositionX < 80) && (mousePositionY > 80) && (mousePositionY < 160)) {
                buttons.getChildren().get(setBoardClickNumber).setLayoutX(0);
                buttons.getChildren().get(setBoardClickNumber).setLayoutY(80);
                grid[1][0] = setBoardClickNumber;
                setBoardClickNumber++;
            } else if ((mousePositionX > 80) && (mousePositionX < 160) && (mousePositionY > 80) && (mousePositionY < 160)) {
                buttons.getChildren().get(setBoardClickNumber).setLayoutX(80);
                buttons.getChildren().get(setBoardClickNumber).setLayoutY(80);
                grid[1][1] = setBoardClickNumber;
                setBoardClickNumber++;
            } else if ((mousePositionX > 160) && (mousePositionX < 240) && (mousePositionY > 80) && (mousePositionY < 160)) {
                buttons.getChildren().get(setBoardClickNumber).setLayoutX(160);
                buttons.getChildren().get(setBoardClickNumber).setLayoutY(80);
                grid[1][2] = setBoardClickNumber;
                setBoardClickNumber++;
            } else if ((mousePositionX > 0) && (mousePositionX < 80) && (mousePositionY > 160) && (mousePositionY < 240)) {
                buttons.getChildren().get(setBoardClickNumber).setLayoutX(0);
                buttons.getChildren().get(setBoardClickNumber).setLayoutY(160);
                grid[2][0] = setBoardClickNumber;
                setBoardClickNumber++;
            } else if ((mousePositionX > 80) && (mousePositionX < 160) && (mousePositionY > 160) && (mousePositionY < 240)) {
                buttons.getChildren().get(setBoardClickNumber).setLayoutX(80);
                buttons.getChildren().get(setBoardClickNumber).setLayoutY(160);
                grid[2][1] = setBoardClickNumber;
                setBoardClickNumber++;
            } else if ((mousePositionX > 160) && (mousePositionX < 240) && (mousePositionY > 160) && (mousePositionY < 240)) {
                buttons.getChildren().get(setBoardClickNumber).setLayoutX(160);
                buttons.getChildren().get(setBoardClickNumber).setLayoutY(160);
                grid[2][2] = setBoardClickNumber;
                setBoardClickNumber++;
            }
        }
    }


    /**
     * Event handler for clicking button 1
     */
    void button1Handler() {
        if (setBoardClickNumber == 10) {
            newBoardString = mainBoard.possibleMoves[1];
            if (newBoardString.length() == 9) {
                mainBoard = new Board(newBoardString);
                mainBoard.printBoard();
                for (int i = 1; i < 9; i++) {
                    buttons.getChildren().get(i).setLayoutX(80 * mainBoard.numberToCoordinateArrayMap[i].col);
                    buttons.getChildren().get(i).setLayoutY(80 * mainBoard.numberToCoordinateArrayMap[i].row);
                }
            }
        }
    }


    /**
     * Event handler for clicking button 2
     */
    void button2Handler() {
        if (setBoardClickNumber == 10) {
            newBoardString = mainBoard.possibleMoves[2];
            if (newBoardString.length() == 9) {
                mainBoard = new Board(newBoardString);
                mainBoard.printBoard();
                for (int i = 1; i < 9; i++) {
                    buttons.getChildren().get(i).setLayoutX(80 * mainBoard.numberToCoordinateArrayMap[i].col);
                    buttons.getChildren().get(i).setLayoutY(80 * mainBoard.numberToCoordinateArrayMap[i].row);
                }
            }
        }
    }


    /**
     * Event handler for clicking button 3
     */
    void button3Handler() {
        if (setBoardClickNumber == 10) {
            newBoardString = mainBoard.possibleMoves[3];
            if (newBoardString.length() == 9) {
                mainBoard = new Board(newBoardString);
                mainBoard.printBoard();
                for (int i = 1; i < 9; i++) {
                    buttons.getChildren().get(i).setLayoutX(80 * mainBoard.numberToCoordinateArrayMap[i].col);
                    buttons.getChildren().get(i).setLayoutY(80 * mainBoard.numberToCoordinateArrayMap[i].row);
                }
            }
        }
    }


    /**
     * Event handler for clicking button 4
     */
    void button4Handler() {
        if (setBoardClickNumber == 10) {
            newBoardString = mainBoard.possibleMoves[4];
            if (newBoardString.length() == 9) {
                mainBoard = new Board(newBoardString);
                mainBoard.printBoard();
                for (int i = 1; i < 9; i++) {
                    buttons.getChildren().get(i).setLayoutX(80 * mainBoard.numberToCoordinateArrayMap[i].col);
                    buttons.getChildren().get(i).setLayoutY(80 * mainBoard.numberToCoordinateArrayMap[i].row);
                }
            }
        }
    }


    /**
     * Event handler for clicking button 5
     */
    void button5Handler() {
        if (setBoardClickNumber == 10) {
            newBoardString = mainBoard.possibleMoves[5];
            if (newBoardString.length() == 9) {
                mainBoard = new Board(newBoardString);
                mainBoard.printBoard();
                for (int i = 1; i < 9; i++) {
                    buttons.getChildren().get(i).setLayoutX(80 * mainBoard.numberToCoordinateArrayMap[i].col);
                    buttons.getChildren().get(i).setLayoutY(80 * mainBoard.numberToCoordinateArrayMap[i].row);
                }
            }
        }
    }


    /**
     * Event handler for clicking button 6
     */
    void button6Handler() {
        if (setBoardClickNumber == 10) {
            newBoardString = mainBoard.possibleMoves[6];
            if (newBoardString.length() == 9) {
                mainBoard = new Board(newBoardString);
                mainBoard.printBoard();
                for (int i = 1; i < 9; i++) {
                    buttons.getChildren().get(i).setLayoutX(80 * mainBoard.numberToCoordinateArrayMap[i].col);
                    buttons.getChildren().get(i).setLayoutY(80 * mainBoard.numberToCoordinateArrayMap[i].row);
                }
            }
        }
    }


    /**
     * Event handler for clicking button 7
     */
    void button7Handler() {
        if (setBoardClickNumber == 10) {
            newBoardString = mainBoard.possibleMoves[7];
            if (newBoardString.length() == 9) {
                mainBoard = new Board(newBoardString);
                mainBoard.printBoard();
                for (int i = 1; i < 9; i++) {
                    buttons.getChildren().get(i).setLayoutX(80 * mainBoard.numberToCoordinateArrayMap[i].col);
                    buttons.getChildren().get(i).setLayoutY(80 * mainBoard.numberToCoordinateArrayMap[i].row);
                }
            }
        }
    }


    /**
     * Event handler for clicking button 8
     */
    void button8Handler() {
        if (setBoardClickNumber == 10) {
            newBoardString = mainBoard.possibleMoves[8];
            if (newBoardString.length() == 9) {
                mainBoard = new Board(newBoardString);
                mainBoard.printBoard();
                for (int i = 1; i < 9; i++) {
                    buttons.getChildren().get(i).setLayoutX(80 * mainBoard.numberToCoordinateArrayMap[i].col);
                    buttons.getChildren().get(i).setLayoutY(80 * mainBoard.numberToCoordinateArrayMap[i].row);
                }
            }
        }
    }


    /**
     * Removes all the button tiles from the screen
     * Resets the number of board clicks
     */
    void setBoardEventHandler() {
        if (setBoardClickNumber==10){
            for (int i =0; i<3; i++) {
                for (int j=0; j<3; j++) {
                    grid[i][j] = 0;
                }
            }

            setBoardClickNumber = 0;

            // create new buttons
            for (int i = 0; i < 9; i++) {
                // set to be off the screen initially
                buttons.getChildren().get(i).setLayoutX(1000);
                buttons.getChildren().get(i).setLayoutY(1000);
            }
        }
    }


    /**
     * This is the handler for the solve button
     * This solves the puzzle and then prints out a new board every 0.3 seconds.
     * The console will display if no solution is available.
     * @throws InterruptedException
     */
    void solveBtnHandler() throws InterruptedException {
        if (setBoardClickNumber != 10) {return;}
        currentNode = new Node(mainBoard);
        solveStartNode = currentNode;
        SearchTree solverTree = new SearchTree(currentNode);
        System.out.println("Solving puzzle automatically...........................");
        while (solverTree.isPQNotEmpty()) {
            if (currentNode.board.heuristicValue == 0) {
                // pass the end node into a function that prints out all boards
                arrangeNodeOrder(solverTree.startNode, currentNode);
                nodeCount=1;
                System.out.println("total node count: " + totalNodes);

                Timeline solver = new Timeline(new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (nodeCount > totalNodes) {return;}
                        else {
                            System.out.format("%d.%n", nodeCount);
                            solveStartNode.board.printBoard();
                            // change tile position here
                            mainBoard = solveStartNode.board;
                            for (int i = 1; i < 9; i++) {
                                buttons.getChildren().get(i).setLayoutX(80 * mainBoard.numberToCoordinateArrayMap[i].col);
                                buttons.getChildren().get(i).setLayoutY(80 * mainBoard.numberToCoordinateArrayMap[i].row);
                            }
                            System.out.println("Heuristic value: " + solveStartNode.board.heuristicValue);
                            System.out.println();
                        }
                        solveStartNode = solveStartNode.getNextNode();
                        nodeCount++;
                    }
                }));
                solver.setCycleCount(Timeline.INDEFINITE);
                solver.play();
                return;
            }
            currentNode.board.generateMovablePieces();
            // adds new children to search tree
            for (String childBoardString: currentNode.board.possibleMoves) {
                if (childBoardString.length() != 0)
                    solverTree.addNode(currentNode, childBoardString);
            }
            currentNode = solverTree.getNextNode();
        }
        System.out.println();
        System.out.println();
        System.out.println("All 181442 moves have been tried.");
        System.out.println("That puzzle is impossible to solve. Best board found was:");
        solverTree.closestNode.board.printBoard();
        System.out.format("Heuristic value: %d %n", solverTree.closestNode.board.heuristicValue);
        System.out.println();
        System.out.println("Exiting program.");
    }


    /**
     * Calls the create functions for lines, buttons, and labels
     */
    void createAllNodes() {
        createLines();
        createSetBoardBtn();
        createSolveBoardBtn();
        createExitBoardBtn();
        createTileButtons();
        createMousePositionLabel();
    }


    /**
     * Groups the event handler for all the buttons together
     */
    void buttonHandlerGroup() {
        buttons.getChildren().get(1).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button1Handler();
            }
        });
        buttons.getChildren().get(2).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button2Handler();

            }
        });
        buttons.getChildren().get(3).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button3Handler();

            }
        });
        buttons.getChildren().get(4).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button4Handler();

            }
        });
        buttons.getChildren().get(5).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button5Handler();
            }
        });
        buttons.getChildren().get(6).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button6Handler();
            }
        });
        buttons.getChildren().get(7).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button7Handler();
            }
        });
        buttons.getChildren().get(8).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button8Handler();
            }
        });
    }


    public void init() {
        System.out.println("Inside the init() method.");
    }


    public void start(Stage primaryStage) {
        System.out.println("Inside the start() method.");

        // create and set the scene here
        Group root = new Group();
        scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        createAllNodes();

        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseMovedHandler(event);
            }
        });
        setBoardBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setBoardEventHandler();
            }
        });
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setButtonsWithClicksHandler();
            }
        });
        solveBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
               try {
                   solveBtnHandler();
               } catch (InterruptedException exc) {
                   System.out.println("Exception caught");
               }
           }
       });
        exitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        primaryStage.close();
                    }
                });

        buttonHandlerGroup();
        root.getChildren().addAll(mousePosition, line1, line2, line3, line4, line5, line6, line7, setBoardBtn, solveBtn, exitBtn, buttons);
        primaryStage.show();
    }


    public void stop() {
        System.out.println("Inside the stop() method.");
    }


    public static void main(String[] args) {
        starterText();
        System.out.println("Launching JavaFX application.");
        launch(args);
    }
}
