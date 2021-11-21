package com.walkover.tablut.search;

import com.walkover.tablut.domain.Action;
import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.State;
import com.walkover.tablut.evaluator.WalkoverEvaluator;
import com.walkover.tablut.evaluator.WalkoverEvaluatorBlack;
import com.walkover.tablut.evaluator.WalkoverEvaluatorWhite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class WalkoverSearch implements Runnable {
    private static int n = 0;
    private ActiveBoard board;
    private Action actionFound;
    private WalkoverEvaluator eval;
    public int nodesExplored;
    public int depthReached;
    private int curN;
    private HashMap<Long, TTEntry> transpositionTable;
    private HashMap<Long, TTEntry> previousTT;


    private int pvCount;
    private int allCount;
    private int cutCount;
    private int repeatCount;

    public WalkoverSearch(ActiveBoard board, WalkoverEvaluator eval){
        n++;
        curN = n;
        this.board = board;
        this.actionFound = null;
        this.eval = eval;
    }

    public float minimax(TreeNode node, int depth, float alpha, float beta){
        if(Thread.currentThread().isInterrupted())
            return 0;
        if(transpositionTable.containsKey(node.state.getGameState().getZobristHash())) {
            TTEntry entry = transpositionTable.get(node.state.getGameState().getZobristHash());
            repeatCount++;
            if (entry.depth >= depth) { //does it give accurate information
                float v = entry.value;
                if (entry.node.t == TreeNode.NodeType.CUT) {
                    if (beta <= v)
                        return v;
                    if (alpha < v)
                        alpha = v;
                }
                if (entry.node.t == TreeNode.NodeType.ALL) {
                    if (v <= alpha)
                        return v;
                    if (v < beta)
                        beta = v;
                }
                if (entry.node.t == TreeNode.NodeType.PV)
                    return v;
            }
        }

        nodesExplored++;
        if(depth == 0 || node.getMaxPlayer().equals(State.Turn.WHITEWIN) || node.getMaxPlayer().equals(State.Turn.BLACKWIN))
            return eval.evaluatePosition(node.state);
        if(node.getMaxPlayer().equals(State.Turn.WHITE)){
            float best = Float.NEGATIVE_INFINITY;
            Action bestAction = null;
            ArrayList<TreeNode> children = node.generateChildren();

            //Use information found in previous search to order them
            ArrayList<TTEntry> childrenAsEntry = new ArrayList<>();
            for(TreeNode c: children){
                if(previousTT.containsKey(c.state.getGameState().getZobristHash())){
                    //Not all information in the TT is correct
                    TTEntry foundEntry = new TTEntry(c, depth, previousTT.get(c.state.getGameState().getZobristHash()).value);
                    childrenAsEntry.add(foundEntry);
                }
                else{
                    childrenAsEntry.add(new TTEntry(c, depth, 0));
                }
            }
            Collections.sort(childrenAsEntry,  Collections.reverseOrder());

            for(TTEntry sortedC: childrenAsEntry){
                TreeNode c = sortedC.node;
                float res = minimax(c, depth -1, alpha, beta);
                if(res > best) {
                    best = res;
                    bestAction = c.pAction;
                }
                if(best > beta){ // pruning condition, CUT-NODE
                    node.t = TreeNode.NodeType.CUT;
                    cutCount++;

                    transpositionTable.put(node.state.getGameState().getZobristHash(),  new TTEntry(node, depth, best));
                    return best;
                }
                alpha = Math.max(alpha, best);
            }
            //If we are in the root record the best child
            if(node.p == null)
                actionFound = bestAction;

            //If we explored everything it's a PV node
            node.t = TreeNode.NodeType.PV;
            pvCount++;
            transpositionTable.put(node.state.getGameState().getZobristHash(),  new TTEntry(node, depth, best));

            return best;
        }
        else if (node.getMaxPlayer().equals(State.Turn.BLACK)){
            float best = Float.POSITIVE_INFINITY;
            Action bestAction = null;
            ArrayList<TreeNode> children = node.generateChildren();
            //Use information found in previous search to order them
            ArrayList<TTEntry> childrenAsEntry = new ArrayList<>();
            for(TreeNode c: children){
                if(previousTT.containsKey(c.state.getGameState().getZobristHash())){
                    //Not all information in the TT is correct
                    TTEntry foundEntry = new TTEntry(c, depth, previousTT.get(c.state.getGameState().getZobristHash()).value);
                    childrenAsEntry.add(foundEntry);
                }
                else{
                    childrenAsEntry.add(new TTEntry(c, depth, 0));
                }
            }
            Collections.sort(childrenAsEntry);

            for(TTEntry sortedC: childrenAsEntry){
                TreeNode c = sortedC.node;

                float res = minimax(c, depth -1, alpha, beta);
                if(res < best) {
                    best = res;
                    bestAction = c.pAction;
                }
                if(best < alpha) { // pruning condition, ALL-NODE
                    node.t = TreeNode.NodeType.ALL;
                    allCount++;
                    transpositionTable.put(node.state.getGameState().getZobristHash(),  new TTEntry(node, depth, best));

                    return best;
                }
                beta = Math.min(beta, best);
            }
            //If we are in the root record the best child
            if(node.p == null)
                actionFound = bestAction;

            //If we explored everything it's a PV node
            node.t = TreeNode.NodeType.PV;
            pvCount++;
            transpositionTable.put(node.state.getGameState().getZobristHash(),  new TTEntry(node, depth, best));

            return best;
        }
        else{
            return 0;
        }

    }


    public float minimaxNoTT(TreeNode node, int depth, float alpha, float beta){
        if(Thread.currentThread().isInterrupted())
            return 0;
        nodesExplored++;
        if(depth == 0 || node.getMaxPlayer().equals(State.Turn.WHITEWIN) || node.getMaxPlayer().equals(State.Turn.BLACKWIN))
            return eval.evaluatePosition(node.state);
        if(node.getMaxPlayer().equals(State.Turn.WHITE)){
            float best = Float.NEGATIVE_INFINITY;
            Action bestAction = null;
            ArrayList<TreeNode> children = node.generateChildren();

            for(TreeNode c: children){
                float res = minimaxNoTT(c, depth -1, alpha, beta);
                if(res > best) {
                    best = res;
                    bestAction = c.pAction;
                }
                if(best > beta){ // pruning condition, CUT-NODE
                    node.t = TreeNode.NodeType.CUT;
                    cutCount++;
                    return best;
                }
                alpha = Math.max(alpha, best);
            }
            //If we are in the root record the best child
            if(node.p == null)
                actionFound = bestAction;

            //If we explored everything it's a PV node
            node.t = TreeNode.NodeType.PV;
            pvCount++;
            return best;
        }
        else if (node.getMaxPlayer() == State.Turn.BLACK){
            float best = Float.POSITIVE_INFINITY;
            Action bestAction = null;

            for(TreeNode c: node.generateChildren()){
                float res = minimaxNoTT(c, depth -1, alpha, beta);
                if(res < best) {
                    best = res;
                    bestAction = c.pAction;
                }
                if(best < alpha) { // pruning condition, ALL-NODE
                    node.t = TreeNode.NodeType.ALL;
                    allCount++;
                    return best;
                }
                beta = Math.min(beta, best);
            }
            //If we are in the root record the best child
            if(node.p == null)
                actionFound = bestAction;

            //If we explored everything it's a PV node
            node.t = TreeNode.NodeType.PV;
            pvCount++;
            return best;
        }
        else{
            return 0;
        }

    }
    @Override
    public void run(){
        int i;
        int behaviourSwitchThreshold = 15;
        int maxDepth = 10;
        transpositionTable = new HashMap<Long, TTEntry>();
        previousTT = new HashMap<Long, TTEntry>();
        //Iterative deepening
        for(i = 1; i < maxDepth; i++) {
            pvCount = 0;
            cutCount = 0;
            allCount = 0;
            repeatCount = 0;

            nodesExplored = 0;
            TreeNode root = new TreeNode(board);
            float res = minimax(root, i, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);

            if((res == Float.POSITIVE_INFINITY && board.getTurn() == State.Turn.WHITE) ||
                    (res == Float.NEGATIVE_INFINITY && board.getTurn() == State.Turn.BLACK)) {
                break;
            }
            if(Thread.currentThread().isInterrupted())
                break;
            depthReached = i;
            //Reset the transposition table
            previousTT = transpositionTable;
            transpositionTable = new HashMap<Long, TTEntry>();

            if(curN > behaviourSwitchThreshold){
                eval.switchBehaviour();
            }

            System.out.println("V:" + res + " N:" + curN + " PV:" + pvCount + " CUT:" + cutCount + " ALL:" + allCount+ " REP:" + repeatCount);
        }

    }

    public Action getResult(){
        return actionFound;
    }
}

class TTEntry implements Comparable{
    public TreeNode node;
    public int depth;
    public float value;

    public TTEntry(TreeNode node, int depth, float value){
        this.node = node;
        this.depth = depth;
        this.value = value;
    }

    @Override
    public int compareTo(Object o) {
        TTEntry other = (TTEntry)o;
        if(value==other.value)
            return 0;
        else if(value>other.value)
            return 1;
        else
            return -1;      }
}

class TreeNode{
    public ActiveBoard state;
    public Action pAction;
    public TreeNode p;
    public NodeType t;

    public enum NodeType {
        PV(), ALL(), CUT();
    }

    public TreeNode(ActiveBoard state, TreeNode p, Action pAction, NodeType type){
        this.p = p;
        this.state = state;
        this.pAction = pAction;
        this.t = type;
    }

    public TreeNode(ActiveBoard state){
        this(state, null, null, null);
    }

    public State.Turn getMaxPlayer(){
        return state.getTurn();
    }

    public ArrayList<TreeNode> generateChildren(){
        ArrayList<TreeNode> children = new ArrayList<TreeNode>();
        for (Action a: state.generateMoves()){
            ActiveBoard newState = new ActiveBoard(state);
            newState.performMove(a);

            children.add(new TreeNode(newState, this, a, null));
        }
        return children;
    }
}