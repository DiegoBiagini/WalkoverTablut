package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class TurtleFormation extends Metric {
    public TurtleFormation(int weight){
        super(weight);
    }
    @Override
    public float evaluate(ActiveBoard board) {
        float startingDensity = (float)16/9;
        // Find all connected components of graph
        ArrayList<Coordinate> whitePawns = board.getWhitePawns();
        whitePawns.add(board.getKing());
        ArrayList<GraphNode> V=new ArrayList<GraphNode>();
        //Build graph
        int i = 0;
        for(Coordinate c: whitePawns) {
            GraphNode cn = new GraphNode(c, new ArrayList<GraphNode>(), i);
            V.add(cn);
            i++;
        }
        for(GraphNode v: V){
            for(GraphNode v2: V){
                if(v.c.getManhattanDist(v2.c) == 1)
                    v.neigh.add(v2);
            }
        }
        // Mark all the vertices as not visited
        boolean[] visited = new boolean[V.size()];
        ArrayList<ArrayList<GraphNode>> ccn = new ArrayList<>();
        for (GraphNode v: V) {
            ArrayList<GraphNode> cc = new ArrayList<>();
            if (!visited[v.id]) {

                DFSUtil(v, visited, cc);
                ccn.add(cc);
            }
            i++;
        }
        float densityMeasure = 0;
        for(ArrayList<GraphNode> cc: ccn){
            float links = 0;
            for(GraphNode v: cc)
                links += v.neigh.size();
            densityMeasure += links/cc.size();
        }
        densityMeasure/= ccn.size();
        return -densityMeasure;

    }

    void DFSUtil(GraphNode v, boolean[] visited, ArrayList<GraphNode> cc) {
        // Mark the current node as visited and print it
        visited[v.id] = true;
        cc.add(v);
        // Recur for all the vertices
        // adjacent to this vertex
        for (GraphNode x : v.neigh) {
            if (!visited[x.id])
                DFSUtil(x, visited, cc);
        }
    }

    class GraphNode{
        public Coordinate c;
        public ArrayList<GraphNode> neigh;
        public int id;

        public GraphNode(Coordinate c, ArrayList<GraphNode> neigh, int id){
            this.c = c;
            this.neigh = neigh;
            this.id = id;
        }
    }
}
