/**
 * Created by zhang4all on 8/30/2016.
 */

/**
 * There is a DijkstraNode for each city in the graph
 * The DijkstraNode stores all the relevant information for each city when you run Dijkstra's algorithm
 */
public class DijkstraNode {
    private int row;
    private int col;
    private int previousRow;
    private int previousCol;
    private int elevationChange;
    private boolean addedToHeap;
    private boolean removedFromHeap;


    /**
     * Constructor for DijkstraNode
     * @param row
     * @param col
     */
    public DijkstraNode(int row, int col){
        this.row = row;
        this.col = col;
        this.elevationChange = Integer.MAX_VALUE;
        this.addedToHeap = false;
        this.removedFromHeap = false;
        this.previousRow = -1;
        this.previousCol = -1;
    }


    public int getRow(){
        return this.row;
    }


    public int getCol() {
        return this.col;
    }


    public void setPreviousRow(int row){
        this.previousRow=row;
    }


    public int getPreviousRow(){
        return this.previousRow;
    }


    public void setPreviousCol(int col){
        this.previousCol=col;
    }


    public int getPreviousCol() {
        return this.previousCol;
    }


    /**
     * sets the elevation change from the source node to the current node
     * @param dist elevationChange from source city to this city
     */
    public void setElevationChange(int elevationChange){
        this.elevationChange = elevationChange;
    }


    /**
     *
     * @return the elevation change from the source node to the current node
     */
    public int getElevationChange(){
        return this.elevationChange;
    }


    /**
     * tells whether this node was removed from the min heap
     * @return
     */
    public boolean getRemovedFromHeap(){
        return this.removedFromHeap;
    }


    /**
     *
     * @return whether this node was added to the min heap
     */
    public boolean getAddedToHeap() {
        return this.addedToHeap;
    }


    /**
     * This is to keep track of what nodes are in the min heap
     * Mark this field true once the node is added to the heap
     */
    public void addToHeap() {
        this.addedToHeap = true;
    }


    /**
     * This is to keep track of what nodes have been removed from the min heap
     * Mark this field true once the node is removed from the min heap
     */
    public void removeFromHeap(){
        this.removedFromHeap = true;
    }
}
