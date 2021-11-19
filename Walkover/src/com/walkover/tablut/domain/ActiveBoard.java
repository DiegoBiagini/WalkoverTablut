package com.walkover.tablut.domain;

import com.walkover.tablut.exceptions.*;

import java.util.ArrayList;

/*
Class that will handle all possible operations applied to the game state
Hopefully faster than the default implementation
 */
public class ActiveBoard{

    private State gameState;
    private Action lastMove;
    private ArrayList<Coordinate> lastTaken;

    private ArrayList<Coordinate> whitePawns;
    private ArrayList<Coordinate> blackPawns;
    private Coordinate king;

    private boolean[][] isCitadel;

    public ActiveBoard(State gameState){
        this.gameState = gameState;
        this.lastMove = null;
        this.lastTaken = new ArrayList<Coordinate>();

        isCitadel = new boolean[gameState.getBoard().length][gameState.getBoard()[0].length];
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
    public ActiveBoard(){
        this(new StateTablut());
    }

    public ActiveBoard(ActiveBoard c1){
        this.gameState = c1.gameState.clone();
        this.lastMove = c1.lastMove;
        this.lastTaken = (ArrayList<Coordinate>)c1.lastTaken.clone();

        this.isCitadel = c1.isCitadel.clone();

        this.king = c1.king;
        this.whitePawns = (ArrayList<Coordinate>)c1.whitePawns.clone();
        this.blackPawns = (ArrayList<Coordinate>)c1.blackPawns.clone();
    }

    /*
    public boolean performMoveOld(Action move) throws PawnException, DiagonalException, ClimbingException, ActionException, CitadelException, StopException, OccupitedException, BoardException, ClimbingCitadelException, ThroneException {
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

    */

    // This function does not check for move validity, for speed reasons ofc
    public void performMove(Action move)  {
        lastTaken.clear();
        State.Turn currentTurn = getTurn();
        Coordinate from = new Coordinate();
        Coordinate to = new Coordinate();

        // MOVE AND CHANGE TURN
        move.toCoordinate(from, to);
        gameState.setPawn(to.r, to.c, gameState.getPawn(from.r, from.c));
        if (from.c == 4 && from.r == 4) {
            gameState.setPawn(from.r, from.c,State.Pawn.THRONE);
        } else {
            gameState.setPawn(from.r, from.c,State.Pawn.EMPTY);
        }
        switchTurn();

        // CHECK CAPTURES
        if (getTurn().equalsTurn("W")) {
            checkCaptureBlack(move);
            checkCaptureBlackKing(move);
        } else if (getTurn().equalsTurn("B")) {
            checkCaptureWhite(move);
        }
        // UPDATE POSITIONS
        if (currentTurn.equals(State.Turn.WHITE)){
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
            for (Coordinate coordinate : lastTaken)
                blackPawns.remove(coordinate);
        }
        else if (currentTurn.equals(State.Turn.BLACK)){
            for(int i = 0; i < blackPawns.size();i++){
                if(blackPawns.get(i).equals(from)){
                    blackPawns.set(i, to);
                    break;
                }
            }
            for (Coordinate coordinate : lastTaken)
                whitePawns.remove(coordinate);
        }

    }

    public ArrayList<Action> generateMoves(){
        int boardLength = 9;
        State.Pawn[][] rawBoard = gameState.getBoard();
        ArrayList<Action> possibleMoves = new ArrayList<>();
        if(getTurn().equals(State.Turn.WHITE)){
            //King
            int newr, newc;
            //Move left
            for(newr = king.r, newc = king.c - 1; newc >= 0; newc --){
                State.Pawn content = rawBoard[newr][newc];
                if(isCitadel[newr][newc])
                    break;
                if(content.equals(State.Pawn.EMPTY))
                    possibleMoves.add(new Action(king, new Coordinate(newr, newc), getTurn()));
                else
                    break;
            }
            //Move right
            for(newr = king.r, newc = king.c + 1; newc < boardLength; newc ++){
                State.Pawn content = rawBoard[newr][newc];
                if(isCitadel[newr][newc])
                    break;
                if(content.equals(State.Pawn.EMPTY))
                    possibleMoves.add(new Action(king, new Coordinate(newr, newc), getTurn()));
                else
                    break;
            }
            //Move up
            for(newr = king.r - 1, newc = king.c; newr >= 0; newr --){
                State.Pawn content = rawBoard[newr][newc];
                if(isCitadel[newr][newc])
                    break;
                if(content.equals(State.Pawn.EMPTY))
                    possibleMoves.add(new Action(king, new Coordinate(newr, newc), getTurn()));
                else
                    break;
            }
            //Move down
            for(newr = king.r + 1, newc = king.c; newr < boardLength; newr ++){
                State.Pawn content = rawBoard[newr][newc];
                if(isCitadel[newr][newc])
                    break;
                if(content.equals(State.Pawn.EMPTY))
                    possibleMoves.add(new Action(king, new Coordinate(newr, newc), getTurn()));
                else
                    break;
            }

            //Other pawns
            for(Coordinate pawn: whitePawns){
                //Move left
                for(newr = pawn.r, newc = pawn.c - 1; newc >=0 ; newc--){
                    State.Pawn content = rawBoard[newr][newc];
                    if(isCitadel[newr][newc])
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possibleMoves.add(new Action(pawn, new Coordinate(newr, newc), getTurn()));
                    else
                        break;
                }
                //Move right
                for(newr = pawn.r, newc = pawn.c + 1; newc < boardLength ; newc++){
                    State.Pawn content = rawBoard[newr][newc];
                    if(isCitadel[newr][newc])
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possibleMoves.add(new Action(pawn, new Coordinate(newr, newc), getTurn()));
                    else
                        break;
                }
                //Move up
                for(newr = pawn.r - 1, newc = pawn.c; newr >=0 ; newr--){
                    State.Pawn content = rawBoard[newr][newc];
                    if(isCitadel[newr][newc])
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possibleMoves.add(new Action(pawn, new Coordinate(newr, newc), getTurn()));
                    else
                        break;
                }
                //Move down
                for(newr = pawn.r + 1, newc = pawn.c; newr < boardLength ; newr++){
                    State.Pawn content = rawBoard[newr][newc];
                    if(isCitadel[newr][newc])
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possibleMoves.add(new Action(pawn, new Coordinate(newr, newc), getTurn()));
                    else
                        break;
                }
            }
        }
        else if(getTurn().equals(State.Turn.BLACK)){
            for(Coordinate pawn: blackPawns){
                int newr = 0, newc;
                //Move left
                for(newr = pawn.r, newc = pawn.c - 1; newc >= 0; newc --){
                    State.Pawn content = rawBoard[newr][newc];
                    if(!isCitadel[pawn.r][pawn.c] && isCitadel[newr][newc] )
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possibleMoves.add(new Action(pawn, new Coordinate(newr, newc), getTurn()));
                    else
                        break;
                }
                //Move right
                for(newr = pawn.r, newc = pawn.c + 1; newc < boardLength; newc++){
                    State.Pawn content = rawBoard[newr][newc];
                    if(!isCitadel[pawn.r][pawn.c] && isCitadel[newr][newc] )
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possibleMoves.add(new Action(pawn, new Coordinate(newr, newc), getTurn()));
                    else
                        break;
                }
                //Move up
                for(newr = pawn.r - 1, newc = pawn.c; newr >= 0; newr --){
                    State.Pawn content = rawBoard[newr][newc];
                    if(!isCitadel[pawn.r][pawn.c] && isCitadel[newr][newc] )
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possibleMoves.add(new Action(pawn, new Coordinate(newr, newc), getTurn()));
                    else
                        break;
                }
                //Move down
                for(newr = pawn.r + 1, newc = pawn.c; newr < boardLength; newr ++){
                    State.Pawn content = rawBoard[newr][newc];
                    if(!isCitadel[pawn.r][pawn.c] && isCitadel[newr][newc] )
                        break;
                    if(content.equals(State.Pawn.EMPTY))
                        possibleMoves.add(new Action(pawn, new Coordinate(newr, newc), getTurn()));
                    else
                        break;
                }
            }
        }
        else{
            System.out.println("Why are you generating moves for an end state?");
        }

        return possibleMoves;
    }

    private void checkCaptureBlack(Action a){
        int colTo = a.getColumnTo();
        int rowTo = a.getRowTo();
        // mangio a destra
        if (colTo < gameState.getBoard().length - 2
                && gameState.getPawn(rowTo, colTo + 1).equalsPawn("W")) {
            if (gameState.getPawn(rowTo, colTo + 2).equalsPawn("B")) {
                gameState.removePawn(rowTo, colTo + 1);
                lastTaken.add(new Coordinate(rowTo, colTo + 1));
            }
            if (gameState.getPawn(rowTo, colTo + 2).equalsPawn("T")) {
                gameState.removePawn(rowTo, colTo + 1);
                lastTaken.add(new Coordinate(rowTo, colTo + 1));
            }
            if (isCitadel[rowTo][colTo + 2]) {
                gameState.removePawn(rowTo, colTo + 1);
                lastTaken.add(new Coordinate(rowTo, colTo + 1));
            }
            if (gameState.getBox(rowTo, colTo + 2).equals("e5")) {
                gameState.removePawn(rowTo, colTo + 1);
                lastTaken.add(new Coordinate(rowTo, colTo + 1));
            }

        }
        // mangio a sinistra
        if (colTo > 1 && gameState.getPawn(rowTo, colTo - 1).equalsPawn("W")
                && (gameState.getPawn(rowTo, colTo - 2).equalsPawn("B")
                || gameState.getPawn(rowTo, colTo - 2).equalsPawn("T")
                || isCitadel[rowTo][colTo - 2]
                || (gameState.getBox(rowTo, colTo - 2).equals("e5")))) {
            gameState.removePawn(rowTo, colTo - 1);
            lastTaken.add(new Coordinate(rowTo, colTo - 1));
        }

        // mangio sopra
        if (rowTo > 1 && gameState.getPawn(rowTo - 1, colTo).equalsPawn("W")
                && (gameState.getPawn(rowTo - 2, colTo).equalsPawn("B")
                || gameState.getPawn(rowTo - 2, colTo).equalsPawn("T")
                || isCitadel[rowTo - 2][ colTo]
                || (gameState.getBox(rowTo - 2, colTo).equals("e5")))) {
            gameState.removePawn(rowTo - 1, colTo);
            lastTaken.add(new Coordinate(rowTo-1, colTo));
        }

        //mangio sotto
        if (rowTo < gameState.getBoard().length - 2
                && gameState.getPawn(rowTo + 1, colTo).equalsPawn("W")
                && (gameState.getPawn(rowTo + 2, colTo).equalsPawn("B")
                || gameState.getPawn(rowTo + 2, colTo).equalsPawn("T")
                || isCitadel[rowTo + 2] [colTo]
                || (gameState.getBox(rowTo + 2, colTo).equals("e5")))) {
            gameState.removePawn(rowTo + 1, colTo);
            lastTaken.add(new Coordinate(rowTo+1, colTo));
        }


    }

    // This is fun
    private void checkCaptureBlackKing(Action a){
        int colTo = a.getColumnTo();
        int rowTo = a.getRowTo();
        // ho il re sulla sinistra
        if (colTo > 1 && gameState.getPawn(rowTo, colTo - 1).equalsPawn("K")) {
            // re sul trono
            if (gameState.getBox(rowTo, colTo - 1).equals("e5")) {
                if (gameState.getPawn(3, 4).equalsPawn("B") && gameState.getPawn(4, 3).equalsPawn("B")
                        && gameState.getPawn(5, 4).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            // re adiacente al trono
            if (gameState.getBox(rowTo, colTo - 1).equals("e4")) {
                if (gameState.getPawn(2, 4).equalsPawn("B") && gameState.getPawn(3, 3).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            if (gameState.getBox(rowTo, colTo - 1).equals("f5")) {
                if (gameState.getPawn(5, 5).equalsPawn("B") && gameState.getPawn(3, 5).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            if (gameState.getBox(rowTo, colTo - 1).equals("e6")) {
                if (gameState.getPawn(6, 4).equalsPawn("B") && gameState.getPawn(5, 3).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            // sono fuori dalle zone del trono
            if (!gameState.getBox(rowTo, colTo - 1).equals("e5")
                    && !gameState.getBox(rowTo, colTo - 1).equals("e6")
                    && !gameState.getBox(rowTo, colTo - 1).equals("e4")
                    && !gameState.getBox(rowTo, colTo - 1).equals("f5")) {
                if (gameState.getPawn(rowTo, colTo - 2).equalsPawn("B")
                        || isCitadel[rowTo][colTo - 2]) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
        }

        // ho il re sulla destra
        if (colTo < gameState.getBoard().length - 2
                && (gameState.getPawn(rowTo, colTo + 1).equalsPawn("K"))) {
            // re sul trono
            if (gameState.getBox(rowTo, colTo + 1).equals("e5")) {
                if (gameState.getPawn(3, 4).equalsPawn("B") && gameState.getPawn(4, 5).equalsPawn("B")
                        && gameState.getPawn(5, 4).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            // re adiacente al trono
            if (gameState.getBox(rowTo, colTo + 1).equals("e4")) {
                if (gameState.getPawn(2, 4).equalsPawn("B") && gameState.getPawn(3, 5).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            if (gameState.getBox(rowTo, colTo + 1).equals("e6")) {
                if (gameState.getPawn(5, 5).equalsPawn("B") && gameState.getPawn(6, 4).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            if (gameState.getBox(rowTo, colTo + 1).equals("d5")) {
                if (gameState.getPawn(3, 3).equalsPawn("B") && gameState.getPawn(5, 3).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            // sono fuori dalle zone del trono
            if (!gameState.getBox(rowTo, colTo + 1).equals("d5")
                    && !gameState.getBox(rowTo, colTo + 1).equals("e6")
                    && !gameState.getBox(rowTo, colTo + 1).equals("e4")
                    && !gameState.getBox(rowTo, colTo + 1).equals("e5")) {
                if (gameState.getPawn(rowTo, colTo + 2).equalsPawn("B")
                        || isCitadel[rowTo][colTo + 2]) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
        }

        // ho il re sotto
        if (rowTo < gameState.getBoard().length - 2
                && gameState.getPawn(rowTo + 1, a.getColumnTo()).equalsPawn("K")) {
            // re sul trono
            if (gameState.getBox(rowTo + 1, colTo).equals("e5")) {
                if (gameState.getPawn(5, 4).equalsPawn("B") && gameState.getPawn(4, 5).equalsPawn("B")
                        && gameState.getPawn(4, 3).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            // re adiacente al trono
            if (gameState.getBox(rowTo + 1, colTo).equals("e4")) {
                if (gameState.getPawn(3, 3).equalsPawn("B") && gameState.getPawn(3, 5).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            if (gameState.getBox(rowTo + 1, colTo).equals("d5")) {
                if (gameState.getPawn(4, 2).equalsPawn("B") && gameState.getPawn(5, 3).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            if (gameState.getBox(rowTo + 1, colTo).equals("f5")) {
                if (gameState.getPawn(4, 6).equalsPawn("B") && gameState.getPawn(5, 5).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            // sono fuori dalle zone del trono
            if (!gameState.getBox(rowTo + 1, colTo).equals("d5")
                    && !gameState.getBox(rowTo + 1, colTo).equals("e4")
                    && !gameState.getBox(rowTo + 1, colTo).equals("f5")
                    && !gameState.getBox(rowTo + 1, colTo).equals("e5")) {
                if (gameState.getPawn(rowTo + 2, colTo).equalsPawn("B")
                        || isCitadel[rowTo + 2][colTo]) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
        }

        // ho il re sopra
        if (rowTo > 1 && gameState.getPawn(rowTo - 1, a.getColumnTo()).equalsPawn("K")) {
            // re sul trono
            if (gameState.getBox(rowTo - 1, colTo).equals("e5")) {
                if (gameState.getPawn(3, 4).equalsPawn("B") && gameState.getPawn(4, 5).equalsPawn("B")
                        && gameState.getPawn(4, 3).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            // re adiacente al trono
            if (gameState.getBox(rowTo - 1, colTo).equals("e6")) {
                if (gameState.getPawn(5, 3).equalsPawn("B") && gameState.getPawn(5, 5).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            if (gameState.getBox(rowTo - 1, colTo).equals("d5")) {
                if (gameState.getPawn(4, 2).equalsPawn("B") && gameState.getPawn(3, 3).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            if (gameState.getBox(rowTo - 1, colTo).equals("f5")) {
                if (gameState.getPawn(4, 6).equalsPawn("B") && gameState.getPawn(3, 5).equalsPawn("B")) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
            // sono fuori dalle zone del trono
            if (!gameState.getBox(rowTo - 1, colTo).equals("d5")
                    && !gameState.getBox(rowTo - 1, colTo).equals("e4")
                    && !gameState.getBox(rowTo - 1, colTo).equals("f5")
                    && !gameState.getBox(rowTo - 1, colTo).equals("e5")) {
                if (gameState.getPawn(rowTo - 2, colTo).equalsPawn("B")
                        || isCitadel[rowTo - 2][colTo]) {
                    gameState.setTurn(State.Turn.BLACKWIN);
                }
            }
        }
    }
    private void checkCaptureWhite(Action a){
        int colTo = a.getColumnTo();
        int rowTo = a.getRowTo();
        // controllo se mangio a destra
        if (colTo < gameState.getBoard().length - 2
                && gameState.getPawn(rowTo, colTo + 1).equalsPawn("B")
                && (gameState.getPawn(rowTo, colTo + 2).equalsPawn("W")
                || gameState.getPawn(rowTo, colTo + 2).equalsPawn("T")
                || gameState.getPawn(rowTo, colTo + 2).equalsPawn("K")
                || (isCitadel[rowTo][colTo + 2]
                && !(colTo + 2 == 8 && rowTo == 4)
                && !(colTo + 2 == 4 && rowTo == 0)
                && !(colTo + 2 == 4 && rowTo == 8)
                && !(colTo + 2 == 0 && rowTo == 4)))) {
            gameState.removePawn(rowTo, colTo + 1);
            lastTaken.add(new Coordinate(rowTo, colTo + 1));
        }
        // controllo se mangio a sinistra
        if (colTo > 1 && gameState.getPawn(rowTo, colTo - 1).equalsPawn("B")
                && (gameState.getPawn(rowTo, colTo - 2).equalsPawn("W")
                || gameState.getPawn(rowTo, colTo - 2).equalsPawn("T")
                || gameState.getPawn(rowTo, colTo - 2).equalsPawn("K")
                || (isCitadel[rowTo][colTo - 2]
                && !(colTo - 2 == 8 && rowTo == 4)
                && !(colTo - 2 == 4 && rowTo == 0)
                && !(colTo - 2 == 4 && rowTo == 8)
                && !(colTo - 2 == 0 && rowTo == 4)))) {
            gameState.removePawn(rowTo, colTo - 1);
            lastTaken.add(new Coordinate(rowTo, colTo - 1));

        }
        // controllo se mangio sopra
        if (rowTo > 1 && gameState.getPawn(rowTo - 1, colTo).equalsPawn("B")
                && (gameState.getPawn(rowTo - 2, colTo).equalsPawn("W")
                || gameState.getPawn(rowTo - 2, colTo).equalsPawn("T")
                || gameState.getPawn(rowTo - 2, colTo).equalsPawn("K")
                || (isCitadel[rowTo - 2][colTo]
                && !(colTo == 8 && rowTo - 2 == 4)
                && !(colTo == 4 && rowTo - 2 == 0)
                && !(colTo == 4 && rowTo - 2 == 8)
                && !(colTo == 0 && rowTo - 2 == 4)))) {
            gameState.removePawn(rowTo - 1, colTo);
            lastTaken.add(new Coordinate(rowTo - 1, colTo));

        }
        // controllo se mangio sotto
        if (rowTo < gameState.getBoard().length - 2
                && gameState.getPawn(rowTo + 1, colTo).equalsPawn("B")
                && (gameState.getPawn(rowTo + 2, colTo).equalsPawn("W")
                || gameState.getPawn(rowTo + 2, colTo).equalsPawn("T")
                || gameState.getPawn(rowTo + 2, colTo).equalsPawn("K")
                || (isCitadel[rowTo + 2][colTo]
                && !(colTo == 8 && rowTo + 2 == 4)
                && !(colTo == 4 && rowTo + 2 == 0)
                && !(colTo == 4 && rowTo + 2 == 8)
                && !(colTo == 0 && rowTo + 2 == 4)))) {
            gameState.removePawn(rowTo + 1, colTo);
            lastTaken.add(new Coordinate(rowTo + 1, colTo));

        }
        // controllo se ho vinto
        if (rowTo == 0 || rowTo == gameState.getBoard().length - 1 || colTo == 0
                || colTo == gameState.getBoard().length - 1) {
            if (gameState.getPawn(rowTo, colTo).equalsPawn("K")) {
                gameState.setTurn(State.Turn.WHITEWIN);
            }
        }

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

        // Beware, row is vertical axis and column is horizontal axis
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

    public ArrayList<Coordinate> getWhitePawns(){
        return whitePawns;
    }

    public ArrayList<Coordinate> getBlackPawns(){
        return blackPawns;
    }

    public Coordinate getKing(){
        return king;
    }

}
