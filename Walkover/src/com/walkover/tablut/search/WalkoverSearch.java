package com.walkover.tablut.search;

import com.sun.source.tree.Tree;
import com.walkover.tablut.domain.Action;
import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.State;
import com.walkover.tablut.evaluator.WalkoverEvaluator;

import java.io.IOException;
import java.util.ArrayList;

public class WalkoverSearch implements Runnable {
    private ActiveBoard board;
    private Action actionFound;
    private WalkoverEvaluator eval;
    private int nodesExplored;

    public WalkoverSearch(ActiveBoard board){
        this.board = board;
        this.actionFound = null;
        this.eval = new WalkoverEvaluator();
    }

    public float minimax(TreeNode node, int depth){
        nodesExplored++;
        if(depth == 0)
            return eval.evaluatePosition(node.state);
        if(node.getMaxPlayer() == State.Turn.WHITE){
            float best = -1000;
            Action bestAction = null;
            for(TreeNode c: node.generateChildren()){
                float res = minimax(c, depth -1);
                if(res > best) {
                    best = res;
                    bestAction = c.pAction;
                }
            }
            //If we are in the root record the best child
            if(node.p == null)
                actionFound = bestAction;
            return best;
        }
        else if (node.getMaxPlayer() == State.Turn.BLACK){
            float best = 1000;
            Action bestAction = null;

            for(TreeNode c: node.generateChildren()){
                float res = minimax(c, depth -1);
                if(res < best) {
                    best = res;
                    bestAction = c.pAction;
                }
            }
            //If we are in the root record the best child
            if(node.p == null)
                actionFound = bestAction;
            return best;
        }
        else if(node.getMaxPlayer() == State.Turn.BLACKWIN){
            return -1;
        }
        else if(node.getMaxPlayer() == State.Turn.WHITEWIN){
            return 1;
        }
        else{
            return 0;
        }

    }

    @Override
    public void run(){
        nodesExplored = 0;
        int depth = 3;
        TreeNode root = new TreeNode(board);
        float result = minimax(root, depth);
        System.out.println(result);
        System.out.println("Nodes explored "  + nodesExplored);
    }

    public Action getResult(){
        return actionFound;
    }
}


class TreeNode{
    public ActiveBoard state;
    public Action pAction;
    public TreeNode p;

    public TreeNode(ActiveBoard state, TreeNode p, Action pAction){
        this.p = p;
        this.state = state;
        this.pAction = pAction;
    }

    public TreeNode(ActiveBoard state){
        this(state, null, null);
    }

    public State.Turn getMaxPlayer(){
        return state.getTurn();
    }

    public ArrayList<TreeNode> generateChildren(){
        ArrayList<TreeNode> children = new ArrayList<TreeNode>();
        for (Action a: state.generateMoves()){
            ActiveBoard newState = new ActiveBoard(state);
            newState.performMove(a);

            children.add(new TreeNode(newState, this, a));
        }
        return children;
    }
}