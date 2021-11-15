package com.walkover.tablut.evaluator;

import com.walkover.tablut.domain.Action;
import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.Coordinate;
import com.walkover.tablut.domain.State;

import java.util.ArrayList;
import java.util.Stack;

//This metric is probably too expensive
public class KingDistanceMetric extends Metric{
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

    private final Coordinate[] goalCoord = new Coordinate[]{
            new Coordinate(1,0),
            new Coordinate(2,0),
            new Coordinate(6,0),
            new Coordinate(7,0),
            new Coordinate(1,8),
            new Coordinate(2,8),
            new Coordinate(6,8),
            new Coordinate(7,8),
            new Coordinate(0,1),
            new Coordinate(0,2),
            new Coordinate(0,6),
            new Coordinate(0,7),
            new Coordinate(8,1),
            new Coordinate(8,2),
            new Coordinate(8,6),
            new Coordinate(8,7),
    } ;

    public KingDistanceMetric(int weight){
        super(weight);
    }
    @Override
    public float evaluate(ActiveBoard board) {
        // Create a simpler board where 0 is empty, 1 is occupied, G is a goal
        char[][] simpleBoard = baseBoard.clone();

        for(Coordinate p: board.getBlackPawns())
            simpleBoard[p.r][p.c] = '1';
        for(Coordinate p: board.getWhitePawns())
            simpleBoard[p.r][p.c] = '1';
        Coordinate kingPos = board.getKing();
        //Perform an A* search
        AStarNode startNode = new AStarNode(kingPos, null, simpleBoard);
        AStarNode goal = aStar(startNode, simpleBoard);
        int kingDistance = 0;
        if (goal == null)
            kingDistance = 1000;
        else
            kingDistance = goal.g;
        AStarNode res = goal;
        while(goal != null){
            System.out.println(goal.c);
            goal = goal.parent;
        }
        return kingDistance;
        /*
        //Now translate the distance in a score
        // For now it's a constant slope with score = 1 at distance 1, score = 0 at distance avgDist, score = -1 at distance 2*avgDist - 1
        int avgDist = 5;

        float score = 1 +(-1 * ((kingDistance - 1)/((float)avgDist - 1)));
        if (score < -1)
            score = -1;
        return score;
        */
    }

    private AStarNode aStar(AStarNode start, char[][] board){
        ArrayList<AStarNode> openList = new ArrayList<AStarNode>();
        ArrayList<AStarNode> closedList = new ArrayList<AStarNode>();

        openList.add(start);
        while(openList.size() > 0){
            int minF = 1000;
            // Find best node
            AStarNode q = null;
            for (AStarNode node: openList) {
                int f = node.getF();
                if (f < minF) {
                    minF = f;
                    q = node;
                }
            }

            openList.remove(q);
            // Generate children
            Coordinate startCoord = q.c;
            ArrayList<AStarNode> possibleMoves = new ArrayList<>();
            int newr, newc;

            //Generate left
            for(newr = startCoord.r, newc = startCoord.c - 1; newc >= 0; newc --){
                char content = board[newr][newc];
                if(content == '1')
                    break;
                else {
                    AStarNode newNode = new AStarNode(new Coordinate(newr, newc), q, board);
                    if (content == 'G') {
                        return newNode;
                    }
                    possibleMoves.add(newNode);
                }
            }
            //Generate right
            for(newr = startCoord.r, newc = startCoord.c + 1; newc < board.length ; newc ++){
                char content = board[newr][newc];
                if(content == '1')
                    break;
                else {
                    AStarNode newNode = new AStarNode(new Coordinate(newr, newc), q, board);
                    if (content == 'G')
                        return newNode;
                    possibleMoves.add(newNode);
                }
            }
            //Generate up
            for(newr = startCoord.r - 1, newc = startCoord.c ; newr >= 0; newr --){
                char content = board[newr][newc];
                if(content == '1')
                    break;
                else {
                    AStarNode newNode = new AStarNode(new Coordinate(newr, newc), q, board);
                    if (content == 'G')
                        return newNode;
                    possibleMoves.add(newNode);
                }
            }
            //Generate down
            for(newr = startCoord.r + 1, newc = startCoord.c ; newr < board.length; newr ++){
                char content = board[newr][newc];
                if(content == '1')
                    break;
                else {
                    AStarNode newNode = new AStarNode(new Coordinate(newr, newc), q, board);
                    if (content == 'G')
                        return newNode;
                    possibleMoves.add(newNode);
                }
            }

            // Remove successors with higher f than nodes in open or closed list
            ArrayList<AStarNode> removedNodes = new ArrayList<>();
            for( AStarNode node: possibleMoves) {
                for(AStarNode opNode : openList){
                    if(node.c.equals(opNode.c) && node.getF() > opNode.getF())
                        removedNodes.add(node);
                }
                for(AStarNode cNode : closedList){
                    if(node.c.equals(cNode.c) && node.getF() > cNode.getF())
                        removedNodes.add(node);
                }

            }

            possibleMoves.removeAll(removedNodes);

            closedList.add(q);
            openList.addAll(possibleMoves);

        }

        return null;
    }

    private int eval(Coordinate c, char[][] board){
        int minDist = 1000;
        for(Coordinate g: goalCoord){
            int dist = c.getManhattanDist(g);
            if(dist < minDist)
                minDist = dist;
        }
        return minDist;
    }

    class AStarNode {
        public Coordinate c;
        public AStarNode parent;
        public int g;
        public int h;

        public AStarNode(Coordinate c, AStarNode parent, char[][] board){
            if(parent == null)
                g = 0;
            else
                g = parent.g + 1;

            this.h = eval(c, board);
            this.c = c;
            this.parent = parent;
        }

        public int getF(){
            return h + g;
        }

    }


}
