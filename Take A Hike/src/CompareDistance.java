/**
 * Created by zhang4all on 8/31/2016.
 */

import java.util.Comparator;


/**
 * Compare object used for the priority queue to implement a min heap
 */
public class CompareDistance implements Comparator<DijkstraNode> {


    /**
     * Comparison function to compare distance of nodes in the graph
     * @param first dijkstra node
     * @param second dijkstra node
     * @return -1 if first < second, 1 if first > second, 0 if equal
     */
    public int compare(DijkstraNode first, DijkstraNode second){
        if (first.getElevationChange() < second.getElevationChange()){
            return -1;
        }

        if (first.getElevationChange() > second.getElevationChange()){
            return 1;
        }

        return 0;
    }
}
