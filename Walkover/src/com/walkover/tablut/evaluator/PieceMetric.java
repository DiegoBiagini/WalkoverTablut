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
