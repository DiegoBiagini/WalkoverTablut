package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.Coordinate;

import java.util.ArrayList;

public class PieceMetric extends Metric{
    private final int totalWhite = 8;
    private final int totalBlack = 16;

    public PieceMetric(int weight){
        super(weight);
    }

    @Override
    public float evaluate(ActiveBoard board) {
        int nWhite = board.getWhitePawns().size();
        int nBlack = board.getBlackPawns().size();

        int rescaledWhite = nWhite * totalBlack/totalWhite;
        return ((float)rescaledWhite) - ((float)nBlack);
    }
}

///////
/*

public class PieceMetric extends Metric{
    private final int totalWhite = 8;
    private final int totalBlack = 16;
    
    private float weightPieces = 0.7;
    private float weightKingSourrounded = 0.2;
    private float weightEscape = 0.1;
    
    public PieceMetric(int weight){
        super(weight);
    }

    @Override
    public float evaluate(ActiveBoard board) {
        int nWhite = board.getWhitePawns().size();
        int nBlack = board.getBlackPawns().size();
        double surrKing = this.KingNeighborhood(state);
        
        int rescaledWhite = nWhite * totalBlack/totalWhite;
        float nPieces=((float)rescaledWhite/(nWhite + nBlack)) - ((float)nBlack/(nWhite + nBlack));
        
        int kingDistToEscape = distanceToClosestEscape(board.king);
		int maxDistToEscape = 6;
		double distToEscape= ((maxDistToEscape-kingDistToEscape)*1.0/maxDistToEscape);  //normalizing 
        
        float res= (nPieces*weightPieces+surrKing*weightKingSourrounded+distToEscape*weightEscape);
        return res;

    }
    
   private double KingNeighborhood(ActiveBoard board){
        double counter = 0;
        ArrayList<Coordinate> neigh = (ArrayList<Coordinate>) Coordinate.getNeighbors(board.king);

        for(Coordinate x: neigh){
            if(board.getPawnAt(x) == Pawn.BLACK || board.getPawnAt(x) == Pawn.THRONE || Coordinates.isCitadel(x)){
                counter++;
            }
        }
        return counter;
    }
    
     public static int distanceToClosestEscape(ActiveBoard board) {
        List<Coordinate> escapes = board.goalCoord;
        int minDistance = Integer.MAX_VALUE;
        for (Coordinate esc : escapes) {
            int distance = board.king.getManhattanDist(esc);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }
}

////////
// In coordinates

public static List<Coordinate> getNeighbors(Coordinate c) {
        List<Coordinate> neighbors = new ArrayList<>();
        for (int incr : Arrays.asList(-1, 1)) {
            try {
                neighbors.add(get(c.x + incr, c.y));
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                neighbors.add(get(c.x, c.y + incr));
            } catch (IndexOutOfBoundsException e) {
            }
        }
        return neighbors;
    }

////
//In active board

    public Pawn getPawnAt(Coordinate c) {
        return board[c.x][c.y];
    }
*/