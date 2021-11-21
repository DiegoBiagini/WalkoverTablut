package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.Action;
import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.Coordinate;
import com.walkover.tablut.domain.State;

//Returns how many free sides the king has, returns a value in [-1,0]
// A free side is a side where the first piece is not an enemy piece
public class KingFreedom extends Metric{
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

    public KingFreedom(int weight){
        super(weight);
    }

    @Override
    public float evaluate(ActiveBoard board) {
        Coordinate king = board.getKing();
        int enemyP = 0;
        State.Pawn[][] rawBoard = board.getGameState().getBoard();
        int newr, newc;
        //Move left
        for(newr = king.r, newc = king.c - 1; newc >= 0; newc --){
            State.Pawn content = rawBoard[newr][newc];
            if(baseBoard[newr][newc]==1 || content == State.Pawn.BLACK) {
                enemyP++;
                break;
            }
            if(content != State.Pawn.EMPTY)
                break;
        }
        //Move right
        for(newr = king.r, newc = king.c + 1; newc < baseBoard.length; newc ++){
            State.Pawn content = rawBoard[newr][newc];
            if(baseBoard[newr][newc]==1 || content == State.Pawn.BLACK) {
                enemyP++;
                break;
            }
            if(content != State.Pawn.EMPTY)
                break;
        }
        //Move up
        for(newr = king.r - 1, newc = king.c; newr >= 0; newr --){
            State.Pawn content = rawBoard[newr][newc];
            if(baseBoard[newr][newc]==1 || content == State.Pawn.BLACK) {
                enemyP++;
                break;
            }
            if(content != State.Pawn.EMPTY)
                break;
        }
        //Move down
        for(newr = king.r + 1, newc = king.c; newr < baseBoard.length; newr ++){
            State.Pawn content = rawBoard[newr][newc];
            if(baseBoard[newr][newc]==1 || content == State.Pawn.BLACK) {
                enemyP++;
                break;
            }
            if(content != State.Pawn.EMPTY)
                break;
        }
        return -(float)enemyP;
    }
}
