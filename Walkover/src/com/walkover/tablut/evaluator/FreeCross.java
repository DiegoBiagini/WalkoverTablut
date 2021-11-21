package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.State;

public class FreeCross extends Metric {
    public FreeCross(int weight){
        super(weight);
    }
    @Override
    public float evaluate(ActiveBoard board) {
        int r = 4;
        int empty=0;
        State.Pawn[][] rawBoard = board.getGameState().getBoard();
        for(int c = 0; c < rawBoard.length; c++){
            if(rawBoard[r][c] == State.Pawn.EMPTY)
                empty++;
        }
        int c = 4;
        for(r = 0; r < rawBoard.length; r++){
            if(rawBoard[r][c] == State.Pawn.EMPTY)
                empty++;
        }
        return empty;
    }
}
