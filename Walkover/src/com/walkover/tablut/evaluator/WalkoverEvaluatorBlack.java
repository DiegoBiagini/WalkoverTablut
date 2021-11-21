package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.State;

import java.util.ArrayList;


public class WalkoverEvaluatorBlack extends WalkoverEvaluator{

    public WalkoverEvaluatorBlack(){
        super();
        //Initialize metrics
        metrics.add(new PieceMetric(20));

        metrics.add(new KingSurrounded(15));
        metrics.add(new KingFreedom(5));
        metrics.add(new KingSupport(5));
        metrics.add(new KingGoodSquares(3));

    }

    @Override
    public void switchBehaviour(){
        metrics = new ArrayList<Metric>();

        metrics.add(new PieceMetric(20));

        metrics.add(new KingSurrounded(15));
        metrics.add(new KingFreedom(5));
        metrics.add(new KingSupport(5));
        metrics.add(new KingGoodSquares(3));

    }


    /*

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


}
