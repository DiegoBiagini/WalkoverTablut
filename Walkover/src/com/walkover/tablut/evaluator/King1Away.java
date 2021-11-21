package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.Coordinate;
import com.walkover.tablut.domain.State;

public class King1Away extends Metric{
    private final char[][] baseBoard = new char[][]{
            {'0','G','G','1','1','1','G','G','0'},
            {'G','0','0','0','1','0','0','0','G'},
            {'G','0','0','0','0','0','0','0','G'},
            {'1','0','0','0','0','0','0','0','1'},
            {'1','1','0','0','0','0','0','1','1'},
            {'1','0','0','0','0','0','0','0','1'},
            {'G','0','0','0','0','0','0','0','G'},
            {'G','0','0','0','1','0','0','0','G'},
            {'0','G','G','1','1','1','G','G','0'},
    };

    public King1Away(int weight){
        super(weight);
    }
    @Override
    public float evaluate(ActiveBoard board) {
        Coordinate king = board.getKing();
        State.Pawn[][] rawBoard = board.getGameState().getBoard();
        int newr, newc;
        //Move left
        for(newr = king.r, newc = king.c - 1; newc >= 0; newc --){
            State.Pawn content = rawBoard[newr][newc];
            if(content != State.Pawn.EMPTY)
                break;
            if(baseBoard[newr][newc] == 'G')
                return 1;
        }
        //Move right
        for(newr = king.r, newc = king.c + 1; newc < baseBoard.length; newc ++){
            State.Pawn content = rawBoard[newr][newc];
            if(content != State.Pawn.EMPTY)
                break;
            if(baseBoard[newr][newc] == 'G')
                return 1;
        }
        //Move up
        for(newr = king.r - 1, newc = king.c; newr >= 0; newr --){
            State.Pawn content = rawBoard[newr][newc];
            if(content != State.Pawn.EMPTY)
                break;
            if(baseBoard[newr][newc] == 'G')
                return 1;
        }
        //Move down
        for(newr = king.r + 1, newc = king.c; newr < baseBoard.length; newr ++){
            State.Pawn content = rawBoard[newr][newc];
            if(content != State.Pawn.EMPTY)
                break;
            if(baseBoard[newr][newc] == 'G')
                return 1;
        }
        return 0;
    }
}
