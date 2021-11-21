package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.Coordinate;

public class KingSupport extends Metric{
    public KingSupport(int weight){
        super(weight);
    }
    @Override
    public float evaluate(ActiveBoard board) {
        int nSupport = 0;
        for(Coordinate c: board.getWhitePawns()) {
            if(Math.abs(c.r-board.getKing().r) <= 1 && Math.abs(c.c - board.getKing().c ) <=1)
                nSupport++;
        }
        return (float)nSupport;
    }
}
