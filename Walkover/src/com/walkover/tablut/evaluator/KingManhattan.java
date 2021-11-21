package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.Coordinate;

//Calculates the minimum Manhattan distance between the king and any escape tiles
//Takes value in [0,1]
public class KingManhattan extends Metric{
    public final Coordinate[] goalCoord = new Coordinate[]{
            new Coordinate(1,0),
            new Coordinate(2,0),
            new Coordinate(6,0),
            new Coordinate(7,0),
            new Coordinate(1,8),
            new Coordinate(2,8),
            new Coordinate(6,8),
            new Coordinate(7,8),
            new Coordinate(0,1),
            new Coordinate(0,2),
            new Coordinate(0,6),
            new Coordinate(0,7),
            new Coordinate(8,1),
            new Coordinate(8,2),
            new Coordinate(8,6),
            new Coordinate(8,7),
    } ;

    public KingManhattan(int weight){
        super(weight);
    }

    @Override
    public float evaluate(ActiveBoard board) {
        int startingDist = 6;
        float minDist = Float.POSITIVE_INFINITY;
        for(Coordinate g: goalCoord){
            int dist = board.getKing().getManhattanDist(g);
            if(dist < minDist)
                minDist = dist;
        }
        return -minDist;
    }
}
