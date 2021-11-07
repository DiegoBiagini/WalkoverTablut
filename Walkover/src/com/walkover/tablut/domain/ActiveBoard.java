package com.walkover.tablut.domain;

import com.walkover.tablut.exceptions.*;

import java.util.ArrayList;

/*
Class that will handle all possible operations applied to the game state
Hopefully faster than the default implementation
 */
public class ActiveBoard {

    private State gameState;
    private Action lastMove;
    private ArrayList<Coordinate> lastTaken;
    private GameAshtonTablut rules;

    private ArrayList<Coordinate> whitePawns;
    private ArrayList<Coordinate> blackPawns;
    private Coordinate king;

    private boolean[][] isCitadel;

    public ActiveBoard(State gameState, GameAshtonTablut rules){
        this.rules = rules;
        this.gameState = gameState;
        this.lastMove = null;
        this.lastTaken = new ArrayList<Coordinate>();

        isCitadel = new boolean[9][9];
        for(int i = 0; i < isCitadel.length; i++){
            for(int j = 0; j < isCitadel.length; j++){
                if(((i == 0 || i == 8) && (j <= 5 && j >= 3))||
                        ((j == 0 || j == 8) && (i <=5 && i>= 3))||
                        (i == 4 && (j == 1 || j == 7))||
                        (j == 4 && (i == 1 || i == 7)))
                    isCitadel[i][j] = true;
                else
                    isCitadel[i][j] = false;
            }
        }

        whitePawns = new ArrayList<Coordinate>();
        blackPawns = new ArrayList<Coordinate>();
        updatePieceLocations();

    }

    public boolean performMove(Action move) throws PawnException, DiagonalException, ClimbingException, ActionException, CitadelException, StopException, OccupitedException, BoardException, ClimbingCitadelException, ThroneException {
        lastTaken.clear();
        State.Turn previousTurn = getTurn();
        //TODO: improve this function
        boolean result = rules.checkMoveAndCapture(gameState, move, lastTaken);
        if (result) {
            // Update the arrays of coordinates
            Coordinate from = new Coordinate();
            Coordinate to = new Coordinate();

            move.toCoordinate(from, to);

            if (previousTurn.equals(State.Turn.WHITE)){
                if(king.equals(from))
                    king = to;
                else{
                    for(int i = 0; i < whitePawns.size();i++){
                        if(whitePawns.get(i).equals(from)){
                            whitePawns.set(i, to);
                            break;
                        }
                    }
                }

                for (Coordinate coordinate : lastTaken) {
                    blackPawns.remove(coordinate);
                }
            }
            else if (previousTurn.equals(State.Turn.BLACK)){
                for(int i = 0; i < blackPawns.size();i++){
                    if(blackPawns.get(i).equals(from)){
                        blackPawns.set(i, to);
                        break;
                    }
                }

                for (Coordinate coordinate : lastTaken) {
                    whitePawns.remove(coordinate);
                }
            }
            System.out.println("Whites");
            for (Coordinate p: whitePawns){
                System.out.println(p);
            }
            System.out.println("King" + king);

            System.out.println("Blacks");
            for(Coordinate p: blackPawns)
                System.out.println(p);

        }

        return result;
    }

    public String revertMove(){
        return "";
    }

    public ArrayList<Action> generateMoves(){
        int boardLength = 9;
        State.Pawn[][] rawBoard = gameState.getBoard();
        ArrayList<Action> possible_moves = new ArrayList<>();
        if(getTurn().equals(State.Turn.WHITE)){
            //King
            int newx, newy;
            //Move left
            for(newx = king.x, newy = king.y - 1; newy >= 0; newy --){
                State.Pawn content = rawBoard[newx][newy];
                if(isCitadel[newx][newy])
                    break;
                if(content.equals(State.Pawn.EMPTY) || content.equals(State.Pawn.THRONE))
                    possible_moves.add(new Action(king, new Coordinate(newx, newy), getTurn()));
                else
                    break;
            }
            //Move right
            for(newx = king.x, newy = king.y + 1; newy < boardLength; newy ++){
                State.Pawn content = rawBoard[newx][newy];
                if(isCitadel[newx][newy])
                    break;
                if(content.equals(State.Pawn.EMPTY) || content.equals(State.Pawn.THRONE))
                    possible_moves.add(new Action(king, new Coordinate(newx, newy), getTurn()));
                else
                    break;
            }
            //Move up
            for(newx = king.x - 1, newy = king.y; newx >= 0; newx --){
                State.Pawn content = rawBoard[newx][newy];
                if(isCitadel[newx][newy])
                    break;
                if(content.equals(State.Pawn.EMPTY) || content.equals(State.Pawn.THRONE))
                    possible_moves.add(new Action(king, new Coordinate(newx, newy), getTurn()));
                else
                    break;
            }
            //Move down
            for(newx = king.x + 1, newy = king.y; newx < boardLength; newx ++){
                State.Pawn content = rawBoard[newx][newy];
                if(isCitadel[newx][newy])
                    break;
                if(content.equals(State.Pawn.EMPTY) || content.equals(State.Pawn.THRONE))
                    possible_moves.add(new Action(king, new Coordinate(newx, newy), getTurn()));
                else
                    break;
            }

            //Other pawns
            for(Coordinate pawn: whitePawns){
                //Move left
                for(newx = pawn.x, newy = pawn.y - 1; newy >=0 ; newy--){
                    State.Pawn content = rawBoard[newx][newy];
                    if(isCitadel[newx][newy])
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possible_moves.add(new Action(pawn, new Coordinate(newx, newy), getTurn()));
                    else
                        break;
                }
                //Move right
                for(newx = pawn.x, newy = pawn.y + 1; newy < boardLength ; newy++){
                    State.Pawn content = rawBoard[newx][newy];
                    if(isCitadel[newx][newy])
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possible_moves.add(new Action(pawn, new Coordinate(newx, newy), getTurn()));
                    else
                        break;
                }
                //Move up
                for(newx = pawn.x - 1, newy = pawn.y; newx >=0 ; newx--){
                    State.Pawn content = rawBoard[newx][newy];
                    if(isCitadel[newx][newy])
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possible_moves.add(new Action(pawn, new Coordinate(newx, newy), getTurn()));
                    else
                        break;
                }
                //Move down
                for(newx = pawn.x + 1, newy = pawn.y; newx < boardLength ; newx++){
                    State.Pawn content = rawBoard[newx][newy];
                    if(isCitadel[newx][newy])
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possible_moves.add(new Action(pawn, new Coordinate(newx, newy), getTurn()));
                    else
                        break;
                }
            }
        }
        else if(getTurn().equals(State.Turn.BLACK)){
            for(Coordinate pawn: blackPawns){
                int newx = 0, newy;
                //Move left
                for(newx = pawn.x, newy = pawn.y - 1; newy >= 0; newy --){
                    State.Pawn content = rawBoard[newx][newy];
                    if(!isCitadel[pawn.x][pawn.y] && isCitadel[newx][newy] )
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possible_moves.add(new Action(pawn, new Coordinate(newx, newy), getTurn()));
                    else
                        break;
                }
                //Move right
                for(newx = pawn.x, newy = pawn.y + 1; newy < boardLength; newy++){
                    State.Pawn content = rawBoard[newx][newy];
                    if(!isCitadel[pawn.x][pawn.y] && isCitadel[newx][newy] )
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possible_moves.add(new Action(pawn, new Coordinate(newx, newy), getTurn()));
                    else
                        break;
                }
                //Move up
                for(newx = pawn.x - 1, newy = pawn.y; newx >= 0; newx --){
                    State.Pawn content = rawBoard[newx][newy];
                    if(!isCitadel[pawn.x][pawn.y] && isCitadel[newx][newy] )
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possible_moves.add(new Action(pawn, new Coordinate(newx, newy), getTurn()));
                    else
                        break;
                }
                //Move down
                for(newx = pawn.x + 1, newy = pawn.y; newx < boardLength; newx ++){
                    State.Pawn content = rawBoard[newx][newy];
                    if(!isCitadel[pawn.x][pawn.y] && isCitadel[newx][newy] )
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possible_moves.add(new Action(pawn, new Coordinate(newx, newy), getTurn()));
                    else
                        break;
                }
            }
        }
        else{
            System.out.println("Why are you generating moves for an end state?");
        }
        return possible_moves;
    }

    public void switchTurn(){
        if(gameState.getTurn() == State.Turn.BLACK)
            gameState.setTurn(State.Turn.WHITE);
        else if(gameState.getTurn() == State.Turn.WHITE)
            gameState.setTurn(State.Turn.BLACK);

    }

    public State getGameState(){
        return gameState;
    }

    public void setGameState(State gameState){
        this.gameState = gameState;
        updatePieceLocations();
    }

    public State.Turn getTurn(){
        return gameState.getTurn();
    }

    /*
    Function used to re compute the location of pieces by scanning the whole board
     */
    private void updatePieceLocations(){
        whitePawns.clear();
        blackPawns.clear();
        king = null;

        // Beware, x is vertical axis and y is horizontal axis
        State.Pawn[][] pawns = gameState.getBoard();
        for(int i = 0; i < pawns.length; i++){
            for(int j = 0; j < pawns.length; j++){
                if(pawns[i][j].equals(State.Pawn.WHITE))
                    whitePawns.add(new Coordinate(i,j));
                else if (pawns[i][j].equals(State.Pawn.BLACK))
                    blackPawns.add(new Coordinate(i,j));
                else if (pawns[i][j].equals(State.Pawn.KING))
                    king = new Coordinate(i,j);
            }
        }
    }
}
