package com.walkover.tablut.domain;

import java.util.Random;

public class ZobristTable {
    private static long[][][] zobristTable = null;
    private static final int boardLength = 9;
    private static final int nPawnTypes = 5;


    public static void generateTable() {
        zobristTable = new long[boardLength][boardLength][nPawnTypes];
        for (int i = 0; i < boardLength; i++)
            for (int j = 0; j < boardLength; j++)
                for (int k = 0; k < nPawnTypes; k++)
                    zobristTable[i][j][k] = new Random().nextLong();
    }

    public static long[][][] getTable() {
        if( zobristTable == null)
            generateTable();
        return zobristTable;
    }
}
