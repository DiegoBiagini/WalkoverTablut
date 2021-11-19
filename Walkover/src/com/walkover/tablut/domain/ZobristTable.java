package com.walkover.tablut.domain;

import java.util.Random;

public class ZobristTable {
    private static long[][][] zobristBoard = null;
    private static long[] zobristTurns = null;
    private static final int boardLength = 9;
    private static final int nPawnTypes = 5;
    private static final int nTurnTypes = 5;


    public static void generateTable() {
        zobristBoard = new long[boardLength][boardLength][nPawnTypes];
        for (int i = 0; i < boardLength; i++) {
            for (int j = 0; j < boardLength; j++)
                for (int k = 0; k < nPawnTypes; k++)
                    zobristBoard[i][j][k] = new Random().nextLong();
        }
        zobristTurns = new long[nTurnTypes];
        for(int i = 0; i < nTurnTypes; i++)
            zobristTurns[i] = new Random().nextLong();
    }

    public static long[][][] getBoardTable() {
        if( zobristBoard == null)
            generateTable();
        return zobristBoard;
    }

    public static long[] getTurnTable(){
        if(zobristTurns == null)
            generateTable();
        return zobristTurns;
    }
}
