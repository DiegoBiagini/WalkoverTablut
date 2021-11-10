package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.ActiveBoard;

/*
Abstract class that defines a way to evaluate a board position
 */
public abstract class Metric {
    private int weight;

    public Metric(int weight){
        this.weight = weight;
    }

    public abstract float evaluate(ActiveBoard board);

    public float evaluateWWeight(ActiveBoard board){
        return weight * evaluate(board);
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public int getWeight(){
        return weight;
    }
}
