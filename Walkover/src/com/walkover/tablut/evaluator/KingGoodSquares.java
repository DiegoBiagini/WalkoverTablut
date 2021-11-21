package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;

public class KingGoodSquares extends Metric{
    private final double[][] promBoard = new double[][]{
            {0, 1, 1, 0, 0, 0, 1, 1, 0},
            {1,.5,.5,-.5, 0, -.5,.5,.5, 1},
            {1,.5,.5, .25, -.5, .25,.5,.5, 1},
            {0,-.75, .25, .1, -.25, .1, .25, -.75, 0},
            {0,     0, -.5, -.25, -.1, -.25, -.5, 0, 0},
            {0,-.75, .25, .1, -.25, .1, .25, -.75, 0},
            {1,.5,.5, .25, -.5, .25,.5,.5, 1},
            {1,.5,.5,-.5, 0, -.5,.5,.5, 1},
            {0, 1, 1, 0, 0, 0, 1, 1, 0},
    };

    public KingGoodSquares(int weight){
        super(weight);
    }
    @Override
    public float evaluate(ActiveBoard board) {
        return (float)promBoard[board.getKing().r][board.getKing().c];
    }
}
