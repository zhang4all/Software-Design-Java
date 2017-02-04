import java.util.*;
import java.io.*;
import java.awt.*;


public class MapDataDrawer
{
    private int[][] grid;
    private int gridLength;
    private int gridWidth;
    private int mapHeight;
    private int mapWidth;
    private Random randomInstance;


    /**
     *
     * @param filename the mapfile
     * @param rows the total number of rows in the file
     * @param cols the total number of columns in the file
     * @throws IOException
     */
    public MapDataDrawer(String filename, int rows, int cols) throws IOException {
        // initialize grid
        // read the data from the file into the grid
        mapHeight = rows;
        mapWidth = cols;

        int rowIterator;
        int colIterator;

        randomInstance = new Random(1);

        File file = new File(filename);

        Scanner sc = new Scanner(file);

        int totalRows = sc.nextInt();
        int totalCols = sc.nextInt();

        grid = new int[totalRows][totalCols];
        gridLength = totalRows;
        gridWidth = totalCols;

        // scans the file information into the grid
        for (rowIterator=0; rowIterator<totalRows; rowIterator++) {
            for (colIterator = 0; colIterator < totalCols; colIterator++) {
                grid[rowIterator][colIterator] = sc.nextInt();
            }
        }
    }


    /**
     * function to give the next random number
     * the random number is between and including 0 and up to and excluding range
     * @param range
     * @return a random integer value
     */
    public int getRandomNumber(int range){
        return randomInstance.nextInt(range);
    }


    public int getGridWidth() {
        return gridWidth;
    }


    /**
    * @return the min value in the entire grid
    */
    public int findMinValue(){
        int min = Integer.MAX_VALUE;
        int rowIterator;
        int colIterator;

        for (rowIterator=0; rowIterator<gridLength; rowIterator++) {
            for (colIterator = 0; colIterator < gridWidth; colIterator++) {
                if (grid[rowIterator][colIterator] < min){
                    min = grid[rowIterator][colIterator];
                }
            }
        }

        return min;
    }


    /**
    * @return the max value in the entire grid
    */
    public int findMaxValue(){
        int max = Integer.MIN_VALUE;
        int rowIterator;
        int colIterator;

        for (rowIterator=0; rowIterator<gridLength; rowIterator++) {
            for (colIterator = 0; colIterator < gridWidth; colIterator++) {
                if (grid[rowIterator][colIterator] > max){
                    max = grid[rowIterator][colIterator];
                }
            }
        }

        return max;
     }


    /**
    * @param col the column of the grid to check
    * @return the index of the row with the lowest value in the given col for the grid
    */
    public  int indexOfMinInCol(int col){
        int min = Integer.MAX_VALUE;
        int minIndex = 0;

        for (int rowIterator=0; rowIterator < gridLength; rowIterator++){
            if (grid[rowIterator][col] < min) {
                min = grid[rowIterator][col];
                minIndex = rowIterator;
            }
        }
        return minIndex;
    }


    /**
    * Draws the grid using the given Graphics object.
    * Colors should be grayscale values 0-255, scaled based on min/max values in grid
    */
    public void drawMap(Graphics g){
        int minElevation = findMinValue();
        int maxElevation = findMaxValue();
        double elevationRange = maxElevation - minElevation;
        double scale = elevationRange/256;

        int rowIterator;
        int colIterator;
        int c;

        for (rowIterator=0; rowIterator<mapHeight; rowIterator++) {
            for (colIterator = 0; colIterator < mapWidth; colIterator++) {

                c = (int) ((grid[rowIterator][colIterator]  - minElevation)/scale);
                if (c == 256){
                    c = 255;
                }
                g.setColor(new Color(c,c,c));
                g.fillRect(colIterator, rowIterator, 1, 1);
            }
        }
    }


    /**
     *
     * @param elevationChanges takes in an arraylist of elevation changes
     * @return the index that contains the minimum elevation change. If there is a tie,
     * randomly choose from the tied elevation changes
     */
    public int getMinElevationChangeIndex(ArrayList<Integer> elevationChanges){
        // gets the minimum elevation change and associated index
        int minElevationChangeIndex = elevationChanges.indexOf(Collections.min(elevationChanges));
        int minElevationChange = elevationChanges.get(minElevationChangeIndex);

        // checks if there is a tie
        ArrayList<Integer> tiedIndices = new ArrayList();
        for (int i=0; i<elevationChanges.size(); i++){
            if (elevationChanges.get(i) == minElevationChange){
                tiedIndices.add(i);
            }
        }

        // choose the index from the tied indices
        if (tiedIndices.size() > 1) {
            int randomIndex = getRandomNumber(tiedIndices.size());
            minElevationChangeIndex = tiedIndices.get(randomIndex);
        }

        return minElevationChangeIndex;
    }


   /**
   * Find a path from West-to-East starting at given row.
   * Choose a forward step out of 3 possible forward locations, using greedy method described in assignment.
   * @return the total change in elevation traveled from West-to-East
   */
    public int drawLowestElevPathLeftToRight(Graphics g, int row){
        int currentElevation, currentColumn, minElevationChangeIndex;
        int elevation1, elevation2, elevation3;
        int currentRow = row;
        int totalElevationChange = 0;

        for (currentColumn = 0; currentColumn < mapWidth-1; currentColumn++){
            g.fillRect(currentColumn, currentRow, 1, 1);

            // calculates the 3 adjacent elevation changes
            currentElevation = grid[currentRow][currentColumn];
            if (currentRow == 0){
                elevation1 = Integer.MAX_VALUE;
            }
            else {
                elevation1 = grid[currentRow-1][currentColumn+1];
            }
            elevation2 = grid[currentRow][currentColumn+1];
            if (currentRow == mapHeight-1){
                elevation3 = Integer.MAX_VALUE;
            }
            else {
                elevation3 = grid[currentRow+1][currentColumn+1];
            }

            // finds the direction that results in minimum elevation change and goes that way
            ArrayList<Integer> elevationChanges = new ArrayList();
            elevationChanges.add(Math.abs(currentElevation-elevation1));
            elevationChanges.add(Math.abs(currentElevation-elevation2));
            elevationChanges.add(Math.abs(currentElevation-elevation3));

            // create a function that finds the minimum and if there is more than one min, it randomly chooses a minimum
            minElevationChangeIndex = getMinElevationChangeIndex(elevationChanges);

            if (minElevationChangeIndex == 0){
                currentRow = currentRow-1;
                totalElevationChange+=elevationChanges.get(0);
            }
            else if (minElevationChangeIndex == 2) {
                currentRow = currentRow + 1;
                totalElevationChange+=elevationChanges.get(2);
            }
            else {
                totalElevationChange+=elevationChanges.get(1);
            }
        }
        g.fillRect(currentColumn, currentRow, 1, 1);

        return totalElevationChange;
    }


    /**
     * Helper method for dijkstra's algorithm
     * Given the current row, current column, adjacent row, and adjacent column,
     * this function updates the adjacent node with the smallest elevation change from the source node
     */
    public void updateDistanceForAdjacentNode(PriorityQueueStr priorityQueue, int currentRow, int currentCol, int adjacentNodeRow, int adjacentNodeCol) {
        int distance;
        int sumDistance;
        int currentNodeElevation = grid[currentRow][currentCol];
        int currentNodeDistance = priorityQueue.peekTopNode().getElevationChange();
        if ((adjacentNodeRow < 0) || (adjacentNodeRow >= mapHeight) || (adjacentNodeCol >= mapWidth)){
            return;
        }
        else if (!priorityQueue.getRemovedFromHeap(adjacentNodeRow, adjacentNodeCol)) {
            distance = Math.abs((grid[adjacentNodeRow][adjacentNodeCol] - currentNodeElevation));
            sumDistance = currentNodeDistance + distance;

            // if never added to heap before, add it to the heap
            if (!priorityQueue.getAddedToHeap(adjacentNodeRow, adjacentNodeCol)) {
                priorityQueue.addNodeToHeap(adjacentNodeRow, adjacentNodeCol);
            }
            // if shorter distance, set the new distance and previous vertex id by removing from heap and then adding back
            if (sumDistance < priorityQueue.getElevationChange(adjacentNodeRow, adjacentNodeCol)){
                priorityQueue.setElevationChange(adjacentNodeRow, adjacentNodeCol, sumDistance);
                priorityQueue.setPreviousVertex(adjacentNodeRow, adjacentNodeCol, currentRow, currentCol);
                priorityQueue.removeNodeFromHeap(adjacentNodeRow, adjacentNodeCol);
                priorityQueue.addNodeToHeap(adjacentNodeRow, adjacentNodeCol);
            }
        }
    }


    /**
     * Helper method for runDijkstra method that draws the smallest elevation change path from a specific row from west to east
     * @param priorityQueue stores the elevation change in each node
     * @return the total change in elevation traveled from West-to-East using dijkstra's algorithm
     */
    public int drawDijkstraLowestElevPath(Graphics g, PriorityQueueStr priorityQueue){
        int minimumElevationChange= Integer.MAX_VALUE;
        int minimumRow=0;
        for (int row=0; row < mapHeight; row++){
            if (priorityQueue.getElevationChange(row, mapWidth-1) < minimumElevationChange){
                minimumElevationChange = priorityQueue.getElevationChange(row, mapWidth-1);
                minimumRow=row;
            }
        }

        int currentRow = minimumRow;
        int currentCol = mapWidth-1;
        DijkstraNode currentVertex = priorityQueue.getVertex(currentRow, currentCol);
        g.fillRect(currentCol, currentRow, 1, 1);
        while (currentVertex.getPreviousCol() != -1){
            currentRow = currentVertex.getRow();
            currentCol = currentVertex.getCol();
            g.fillRect(currentCol, currentRow, 1, 1);
            currentVertex = priorityQueue.getPreviousVertex(currentRow, currentCol);
        }

        return priorityQueue.getElevationChange(minimumRow, mapWidth-1);
    }


    /**
     *
     * runs dijkstra's algorithm to get minimum elevation change from starting node to every node in the graph
     * @param sourceRow starting row on leftmost column
     * @return the total change in elevation traveled from West-to-East using dijkstra's algorithm
     */
    public int runDijkstra(Graphics g, int sourceRow) {

        int currentRow = sourceRow;
        int currentCol = 0;
        int adjacentNodeRow, adjacentNodeCol;
        DijkstraNode currentNode;

        PriorityQueueStr priorityQueue = new PriorityQueueStr(currentRow, currentCol, mapHeight, mapWidth);
        // adds the source vertex to the heap
        priorityQueue.addNodeToHeap(sourceRow, currentCol);
        // get the dijkstra node object for the start city
        currentNode = priorityQueue.peekTopNode();
        while ((priorityQueue.getCurrentHeapSize() != 0 ) && (currentNode.getElevationChange() != Integer.MAX_VALUE) && (currentCol < mapWidth)) {
            // sets row, col, and distance of current node
            currentRow = currentNode.getRow();
            currentCol = currentNode.getCol();
            // traverse through all adjacent nodes
            // for adjacent node 1
            adjacentNodeRow = currentRow-1;
            adjacentNodeCol = currentCol;
            updateDistanceForAdjacentNode(priorityQueue, currentRow, currentCol,adjacentNodeRow, adjacentNodeCol);
            // for adjacent node 2
            adjacentNodeRow = currentRow-1;
            adjacentNodeCol = currentCol+1;
            updateDistanceForAdjacentNode(priorityQueue, currentRow, currentCol,adjacentNodeRow, adjacentNodeCol);
            // for adjacent node 3
            adjacentNodeRow = currentRow;
            adjacentNodeCol = currentCol+1;
            updateDistanceForAdjacentNode(priorityQueue, currentRow, currentCol,adjacentNodeRow, adjacentNodeCol);
            // for adjacent node 4
            adjacentNodeRow = currentRow+1;
            adjacentNodeCol = currentCol+1;
            updateDistanceForAdjacentNode(priorityQueue, currentRow, currentCol,adjacentNodeRow, adjacentNodeCol);
            // for adjacent node 5
            adjacentNodeRow = currentRow+1;
            adjacentNodeCol = currentCol;
            updateDistanceForAdjacentNode(priorityQueue, currentRow, currentCol,adjacentNodeRow, adjacentNodeCol);

            // this removes it from the heap
            priorityQueue.removeTopNode();
            currentNode = priorityQueue.peekTopNode();
            if (currentNode == null){break;}
        }

        return drawDijkstraLowestElevPath(g, priorityQueue);
    }


    /**
     * Runs dijkstra's algorithm on every row in the leftmost column
     * This gives the starting row number in column 0 for my best solution
     * @return the index of the starting row for the dijkstra path with the lowest elevation change in the entire grid
     */
    public int indexOfLowestElevPathDijkstra(Graphics g) {
        int currentRow;
        int minTotalElevationChange = Integer.MAX_VALUE;
        int minTotalElevationChangeIndex = -1;
        int currentTotalElevationChange;

        for (currentRow=0; currentRow<mapHeight; currentRow++){
            currentTotalElevationChange = runDijkstra(g,currentRow);
            if (currentTotalElevationChange < minTotalElevationChange){
                minTotalElevationChange = currentTotalElevationChange;
                minTotalElevationChangeIndex = currentRow;
            }
        }

        return minTotalElevationChangeIndex;
    }


    /**
     * Another solution for part 6, not used for final program
     * Find a path from East-to-West starting at given row.
     * Choose a forward step out of 3 possible forward locations, using greedy method described in assignment.
     * @return the total change in elevation traveled from East-to-West
     */
    public int drawLowestElevPathRightToLeft(Graphics g, int row){
        int currentElevation, currentColumn, minElevationChangeIndex;
        int elevation1, elevation2, elevation3;
        int currentRow = row;
        int totalElevationChange = 0;

        for (currentColumn = gridWidth-1; currentColumn > 0; currentColumn--){
            g.fillRect(currentColumn, currentRow, 1, 1);

            // calculates the 3 adjacent elevation changes
            currentElevation = grid[currentRow][currentColumn];
            if (currentRow == 0){
                elevation1 = Integer.MAX_VALUE;
            }
            else {
                elevation1 = grid[currentRow-1][currentColumn-1];
            }
            elevation2 = grid[currentRow][currentColumn-1];
            if (currentRow == mapHeight-1){
                elevation3 = Integer.MAX_VALUE;
            }
            else {
                elevation3 = grid[currentRow+1][currentColumn-1];
            }

            // finds the direction that results in minimum elevation change and goes that way
            ArrayList<Integer> elevationChanges = new ArrayList();
            elevationChanges.add(Math.abs(currentElevation-elevation1));
            elevationChanges.add(Math.abs(currentElevation-elevation2));
            elevationChanges.add(Math.abs(currentElevation-elevation3));
            minElevationChangeIndex = getMinElevationChangeIndex(elevationChanges);

            if (minElevationChangeIndex == 0){
                currentRow = currentRow-1;
                totalElevationChange+=elevationChanges.get(0);
            }
            else if (minElevationChangeIndex == 2) {
                currentRow = currentRow + 1;
                totalElevationChange+=elevationChanges.get(2);
            }
            else {
                totalElevationChange+=elevationChanges.get(1);
            }
        }
        g.fillRect(currentColumn, currentRow, 1, 1);

        return totalElevationChange;
    }


    /**
     *
     *  This gives the starting row number for the best greedy path
    * @return the index of the starting row for the lowest-elevation-change path in the entire grid.
    */
    public int indexOfLowestElevPath(Graphics g){
    int currentRow;
    int minTotalElevationChange = Integer.MAX_VALUE;
    int minTotalElevationChangeIndex = -1;
    int currentTotalElevationChange;

    for (currentRow=0; currentRow<mapHeight; currentRow++){
        currentTotalElevationChange = drawLowestElevPathLeftToRight(g,currentRow);
        if (currentTotalElevationChange < minTotalElevationChange){
            minTotalElevationChange = currentTotalElevationChange;
            minTotalElevationChangeIndex = currentRow;
        }
    }
    return minTotalElevationChangeIndex;
}


    /**
     * Another solution for part 6, not used for final program
     * This gives the starting row number of the best greedy path going from right to left
     * @return
     */
    public int indexOfLowestElevPathRightToLeft(Graphics g){
        int currentRow;
        int minTotalElevationChange = Integer.MAX_VALUE;
        int minTotalElevationChangeIndex = -1;
        int currentTotalElevationChange;

        for (currentRow=0; currentRow<mapHeight; currentRow++){
            currentTotalElevationChange = drawLowestElevPathRightToLeft(g,currentRow);
            if (currentTotalElevationChange < minTotalElevationChange){
                minTotalElevationChange = currentTotalElevationChange;
                minTotalElevationChangeIndex = currentRow;
            }
        }
        return minTotalElevationChangeIndex;
    }
}