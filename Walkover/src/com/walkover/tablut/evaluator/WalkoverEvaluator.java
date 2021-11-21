package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.State;

import java.util.ArrayList;

public class WalkoverEvaluator {
    protected ArrayList<Metric> metrics;

    public WalkoverEvaluator(){
        metrics = new ArrayList<>();
    }

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

    public void switchBehaviour(){
    }
}
