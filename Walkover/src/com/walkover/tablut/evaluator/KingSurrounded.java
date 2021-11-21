package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.Coordinate;
import com.walkover.tablut.domain.State;

//Counts how many black pawns or throne or citadels are surrounding the king
//Returns value in [-1,0]
public class KingSurrounded extends Metric{
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

    public KingSurrounded(int weight){
        super(weight);
    }

    @Override
    public float evaluate(ActiveBoard board) {
        Coordinate kPos = board.getKing();
        int surround = 0;
        State.Pawn[][] rawBoard = board.getGameState().getBoard();
        if(kPos.r -1 >= 0)
            surround +=
                    rawBoard[kPos.r - 1][kPos.c] == State.Pawn.BLACK ||
                    rawBoard[kPos.r - 1][kPos.c] == State.Pawn.THRONE ||
                    baseBoard[kPos.r - 1][kPos.c] == 1 ?0: 1;
        if(kPos.r +1 < rawBoard.length)
            surround +=
                    rawBoard[kPos.r + 1][kPos.c] == State.Pawn.BLACK ||
                    rawBoard[kPos.r + 1][kPos.c] == State.Pawn.THRONE ||
                    baseBoard[kPos.r + 1][kPos.c] == 1 ?0: 1;
        if(kPos.c -1 >= 0)
            surround -=
                    rawBoard[kPos.r][kPos.c-1] == State.Pawn.BLACK ||
                    rawBoard[kPos.r][kPos.c-1] == State.Pawn.THRONE ||
                    baseBoard[kPos.r][kPos.c-1] == 1 ?0: 1;
        if(kPos.c +1 < rawBoard.length)
            surround -=
                    rawBoard[kPos.r][kPos.c+1] == State.Pawn.BLACK ||
                    rawBoard[kPos.r][kPos.c+1] == State.Pawn.THRONE ||
                    baseBoard[kPos.r][kPos.c+1] == 1 ?0: 1;
        return -(float)surround;
    }
}
