package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.State;

import java.util.ArrayList;


public class WalkoverEvaluatorWhite {
    private ArrayList<Metric> metrics;

    public WalkoverEvaluatorWhite(){
        metrics = new ArrayList<Metric>();
        //Initialize metrics
        metrics.add(new PieceMetric(50));
        metrics.add(new KingSurrounded(10));
        metrics.add(new KingGoodSquares(10));
        metrics.add(new FreeCross(5));


    }

    public void switchBehaviour(){
        metrics = new ArrayList<Metric>();

        metrics.add(new PieceMetric(20));
        metrics.add(new KingSurrounded(5));
        metrics.add(new KingSupport(5));
        metrics.add(new KingFreedom(5));
        metrics.add(new FreeCross(3));
    }

    /* GOOD AS BLACK
        metrics.add(new PieceMetric(25));

        metrics.add(new KingSurrounded(9));
        metrics.add(new KingFreedom(3));
        metrics.add(new KingSupport(3));
        metrics.add(new KingGoodSquares(3));

        Good DRAW AS WHITE
        metrics.add(new PieceMetric(50));
        metrics.add(new KingSurrounded(10));
        metrics.add(new KingFreedom(5));
        metrics.add(new KingSupport(5));
        metrics.add(new KingGoodSquares(10));
        metrics.add(new FreeCross(3));

        Good as white
        if(late) {
            metrics.add(new PieceMetric(20));
            metrics.add(new KingSurrounded(5));
            metrics.add(new KingSupport(5));
            metrics.add(new KingFreedom(5));
            metrics.add(new FreeCross(3));
        }
        else{
            metrics.add(new PieceMetric(50));
            metrics.add(new KingSurrounded(10));
            metrics.add(new KingGoodSquares(10));
            metrics.add(new FreeCross(5));
        }

     */

    public float evaluatePosition(ActiveBoard board){
        if(board.getTurn().equals(State.Turn.WHITEWIN))
            return Float.POSITIVE_INFINITY;
        if(board.getTurn().equals(State.Turn.BLACKWIN))
            return Float.NEGATIVE_INFINITY;

        int totalWeight = 0;
        float totalScore = 0;
        for(Metric m : metrics){
            totalWeight += m.getWeight();
            totalScore += m.evaluateWWeight(board);
        }
        //System.out.println(totalScore/totalWeight);
        return totalScore/totalWeight;
    }
}
