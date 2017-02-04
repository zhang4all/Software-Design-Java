/**
 * Created by zhang4all on 8/30/2016.
 */

import java.util.PriorityQueue;
import java.util.Comparator;


/**
 * This is a structure that stores a reference to an array and a priority queue
 */
public class PriorityQueueStr {
    private int sourceRow;
    private int sourceCol;
    private int totalRows;
    private int totalCols;
    private int currentHeapSize;
    private int sizeOfGrid;
    private DijkstraNode[][] nodeArray;
    private PriorityQueue<DijkstraNode> pq;


    /**
     * Constructor
     * @param sourceRow the starting row
     * @param sourceCol the starting column
     * @param numRows total number of rows in grid
     * @param numCols total number of columns in grid
     */
    public PriorityQueueStr(int sourceRow, int sourceCol, int numRows, int numCols) {
        // always start in column 0
        // we can chose the row to start in
        this.sourceRow = sourceRow;
        this.sourceCol = sourceCol;
        this.totalRows = numRows;
        this.totalCols = numCols;
        this.sizeOfGrid = numRows*numCols;
        this.currentHeapSize = numRows*numCols;
        this.nodeArray = new DijkstraNode[numRows][numCols];

        // the following for loop creates the array of nodes used for dijkstra's
        for (int i=0; i < totalRows; i++){
            for (int j=0; j< totalCols; j++){
                this.nodeArray[i][j] = new DijkstraNode(i,j);
            }
        }

        this.nodeArray[sourceRow][sourceCol].setElevationChange(0);

        Comparator<DijkstraNode> comparer = new CompareDistance();

        // construct the priority queue
        this.pq = new PriorityQueue(this.sizeOfGrid, comparer);
    }



    public void addNodeToHeap(int row, int col){
        this.nodeArray[row][col].addToHeap();
        this.pq.add(this.nodeArray[row][col]);
    }


    public void removeNodeFromHeap(int row, int col) {
        this.pq.remove(this.nodeArray[row][col]);
    }


    public void setPreviousVertex(int currentRow, int currentCol, int previousRow, int previousCol) {
        this.nodeArray[currentRow][currentCol].setPreviousRow(previousRow);
        this.nodeArray[currentRow][currentCol].setPreviousCol(previousCol);
    }


    public DijkstraNode getVertex(int Row, int Col){
        return this.nodeArray[Row][Col];
    }


    public DijkstraNode getPreviousVertex(int row, int col){
        int previousRow = this.nodeArray[row][col].getPreviousRow();
        int previousCol = this.nodeArray[row][col].getPreviousCol();
        return this.nodeArray[previousRow][previousCol];
    }

    /**
     *
     * @return the number of nodes in the min heap
     */
    public int getCurrentHeapSize() {
        return this.currentHeapSize;
    }


    public boolean getRemovedFromHeap(int row, int col) {
        return this.nodeArray[row][col].getRemovedFromHeap();
    }


    public boolean getAddedToHeap(int row, int col) {
        return this.nodeArray[row][col].getAddedToHeap();
    }


    public int getElevationChange(int row, int col) {
        return this.nodeArray[row][col].getElevationChange();
    }


    public void setElevationChange(int row, int col, int elevationChange) {
        this.nodeArray[row][col].setElevationChange(elevationChange);
    }


    public DijkstraNode removeTopNode(){
        this.pq.peek().removeFromHeap();
        return this.pq.poll();
    }


    /**
     * Gets the Dijkstra Node with the minimum distance in the priority queue
     * @return the Dijkstra Node with the minimum distance in the priority queue
     */
    public DijkstraNode peekTopNode() {
        return this.pq.peek();
    }
}
