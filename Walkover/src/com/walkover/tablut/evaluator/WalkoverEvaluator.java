package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;

import java.util.ArrayList;


public class WalkoverEvaluator {
    private ArrayList<Metric> metrics;

    public WalkoverEvaluator(){
        metrics = new ArrayList<Metric>();
        //Initialize metrics
        metrics.add(new PieceMetric(2));
        metrics.add(new KingDistanceMetric(1));


    }

    public float evaluatePosition(ActiveBoard board){
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
