package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;

import java.util.ArrayList;


public class WalkoverEvaluator {
    private ActiveBoard board;

    private ArrayList<Metric> metrics;

    public WalkoverEvaluator(ActiveBoard board){
        this.board = board;

        metrics = new ArrayList<Metric>();
        //Initialize metrics
        metrics.add(new PieceMetric(1));

    }

    public float evaluatePosition(){
        int totalWeight = 0;
        float totalScore = 0;
        for(Metric m : metrics){
            totalWeight += m.getWeight();
            totalScore += m.evaluateWWeight(board);
        }

        return totalScore/totalWeight;
    }

    public void setBoard(ActiveBoard board){
        this.board = board;
    }
}
