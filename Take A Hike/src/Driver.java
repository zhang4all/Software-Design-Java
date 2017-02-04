/**
 * Program 2: Take a Hike
 *
 * Class: CS 342, Fall 2016
 * System Windows, IntelliJ IDE
 * Author Code Number: 823
 *
 */


import java.awt.*;
import java.io.IOException;


public class Driver
{
    /**
     * another possible solution for part 6. Not used in final program
     * @param map
     * @param g
     */
    public static void partSixRightToLeft(MapDataDrawer map, Graphics g){
        map.drawMap(g); //use this to get rid of all red lines
        g.setColor(Color.GREEN); //set brush to green for drawing best path
        int minRow = map.indexOfMinInCol(map.getGridWidth() - 1);
        System.out.println("Row with lowest val in col 0: "+minRow);
        int totalChange = map.drawLowestElevPathRightToLeft(g, minRow); //use minRow from Step 2 as starting point
        System.out.println("Lowest-Elevation-Change Path starting at row "+minRow+" gives total change of: "+totalChange);

        g.setColor(Color.RED);
        int bestRow = map.indexOfLowestElevPathRightToLeft(g);

        //map.drawMap(g); //use this to get rid of all red lines
        g.setColor(Color.GREEN); //set brush to green for drawing best path
        totalChange = map.drawLowestElevPathRightToLeft(g, bestRow);
        System.out.println("The Lowest-Elevation-Change Path starts at row: "+bestRow+" and gives a total change of: "+totalChange);
    }


    public static void main(String[] args) throws IOException {
        // Header
        System.out.println("Author Code Number: 823");
        System.out.println("Class: CS 342, Fall 2016");
        System.out.println("Program: #2, Take a Hike");
        System.out.println();


        //construct DrawingPanel, and get its Graphics context
        DrawingPanel panel = new DrawingPanel(212, 117);
        Graphics g = panel.getGraphics();

        //Test Step 1 - construct mountain map data
        // "Colorado_844x480.dat"
        // "NevadaToCalifornia_212x117.dat"
        MapDataDrawer map = new MapDataDrawer("NevadaToCalifornia.txt", 117, 212);

        //Test Step 2 - min, max, minRow in col
        int min = map.findMinValue();
        System.out.println("Min value in map: "+min);
        int max = map.findMaxValue();
        System.out.println("Max value in map: "+max);
        int minRow = map.indexOfMinInCol(0);
        System.out.println("Row with lowest val in col 0: "+minRow);

        //Test Step 3 - draw the map
        map.drawMap(g);

        //Test Step 4 - draw a greedy path
        g.setColor(Color.RED); //can set the color of the 'brush' before drawing, then method doesn't need to worry about it
        int totalChange = map.drawLowestElevPathLeftToRight(g, minRow); //use minRow from Step 2 as starting point
        System.out.println("Greedy path starting at lowest row "+minRow+" gives total change of: "+totalChange);

        //Test Step 5 - draw the best path
        g.setColor(Color.GREEN);
        int bestRow = map.indexOfLowestElevPath(g);

        map.drawMap(g); //use this to get rid of all red lines
        g.setColor(Color.GREEN); //set brush to green for drawing best path
        totalChange = map.drawLowestElevPathLeftToRight(g, bestRow);
        System.out.println("The best greedy path at row: "+bestRow+" and gives a total change of: "+totalChange);

        // Test Step 6 - draw my best solution using dijkstra's
        map.drawMap(g); //use this to get rid of all red lines
        g.setColor(Color.BLUE);
        map.runDijkstra(g, 15);

        map.drawMap(g); //use this to get rid of all red lines
        int myRow = map.indexOfLowestElevPathDijkstra(g);
        totalChange = map.runDijkstra(g, myRow);
        System.out.println("My best solution using dijkstra's algorithm starts at row: "+myRow+" and gives a total change of: "+totalChange);

        // final output:
        map.drawMap(g); //use this to get rid of all red lines
        g.setColor(Color.RED);
        map.drawLowestElevPathLeftToRight(g, minRow);
        g.setColor(Color.GREEN);
        map.drawLowestElevPathLeftToRight(g, bestRow);
        g.setColor(Color.BLUE);
        map.runDijkstra(g, myRow);
    }
}