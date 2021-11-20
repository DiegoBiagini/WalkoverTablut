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

//Heuristics

//Black
int whitePieceEaten = state.getNumberPlayerPieces(BoardState.WHITE)-state.getNumberPlayerPieces(BoardState.WHITE);
int blackPieceCount = state.getNumberPlayerPieces(BoardState.BLACK);
double  pawnsNearKing = (double)  checkNearPawns(state, kingPosition(state),State.Turn.BLACK.toString()) / getNumEatenPositions(state);
    public int checkNearPawns(State state, int[] position, String target){
        int count=0;
        //GET TURN
        State.Pawn[][] board = state.getBoard();
        if(board[position[0]-1][position[1]].equalsPawn(target))
            count++;
        if(board[position[0]+1][position[1]].equalsPawn(target))
            count++;
        if(board[position[0]][position[1]-1].equalsPawn(target))
            count++;
        if(board[position[0]][position[1]+1].equalsPawn(target))
            count++;
        return count;
    }

//White
int whitePieceCount = state.getNumberPlayerPieces(BoardState.WHITE);
int blackPieceEaten = state.getNumberPlayerPieces(BoardState.BLACK)-state.getNumberPlayerPieces(BoardState.BLACK);
 //pawns near king(?)
//number of escapes which king can reach
    public int countWinWays(State state){
        int[] kingPosition=this.kingPosition(state);
        int col = 0;
        int row = 0;
        if(!safePositionKing(state,kingPosition)){
            if((!(kingPosition[1] > 2 && kingPosition[1] < 6)) && (!(kingPosition[0] > 2 && kingPosition[0] < 6))){
                //not safe row not safe col
                col = countFreeColumn(state, kingPosition);
                row = countFreeRow(state,kingPosition);
            }
            if((kingPosition[1] > 2 && kingPosition[1] < 6)){
                // safe row not safe col
                row = countFreeRow(state, kingPosition);
            }
            if((kingPosition[0] > 2 && kingPosition[0] < 6)) {
                // safe col not safe row
                col = countFreeColumn(state, kingPosition);
            }
            //System.out.println("ROW:"+row);
            //System.out.println("COL:"+col);
            return (col + row);
        }

        return (col + row);

    }

public double victoryPaths(Coord king, List<Coord> blackPieces, List<Coord> whitePieces) {

        List<Coord> victoryPos = victoryRoads(king);

        if(victoryPos.isEmpty()) return 0;

        double paths=0;
        Optional<Coord> o;
        Supplier<Stream<Coord>> sup1, sup2;

        sup1 = () -> Stream.concat(blackPieces.stream(), whitePieces.stream());
        sup2 = () -> Stream.concat(sup1.get(), citadels.stream());

        //controllo che non ci siano pezzi/cittadelle fra il re e le victorypos
        for(Coord victory : victoryPos){
            o = sup2.get().filter(p -> isBetween(p, victory, king)).findAny();

            if(!o.isPresent()) paths++;
        }
        return paths;
    }

    //piece - victory - king
    private boolean isBetween(Coord p, Coord victory, Coord king){
        double max, min;

        if(p.equals((Coord)king)) return false;
        if(p.equals((Coord)victory)) return true;

        //controllo le colonne
        if(victory.getCol() == king.getCol()){

            if(p.getCol() != king.getCol()) return false;

            max = (victory.getRow() >= king.getRow() ? victory.getRow() : king.getRow());
            min = (victory.getRow() < king.getRow() ? victory.getRow() : king.getRow());

            return p.getRow() > min && p.getRow() < max;
        }
//Manhattan distance king to the nearest escaping tile (?)
