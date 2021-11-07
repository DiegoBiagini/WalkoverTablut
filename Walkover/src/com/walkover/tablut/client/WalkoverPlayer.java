package com.walkover.tablut.client;

import com.walkover.tablut.domain.*;
import com.walkover.tablut.exceptions.*;

import java.io.IOException;
import java.util.ArrayList;

public class WalkoverPlayer {
    public static void main(String[] args) throws IOException, ClassNotFoundException, PawnException, DiagonalException, ClimbingException, ActionException, CitadelException, StopException, OccupitedException, BoardException, ClimbingCitadelException, ThroneException {
        //WalkoverClient.main(args);

        State state = new StateTablut();
        state.setTurn(State.Turn.WHITE);
        GameAshtonTablut rules = new GameAshtonTablut(state, 99, 100, "folder", "a", "b");

        ActiveBoard board = new ActiveBoard(state, rules);
        /*
        ArrayList<Action> possible_moves = board.generateMoves();
        for(Action a: possible_moves)
            System.out.println(a);
            */
        Action firstA = new Action("e3", "f3", State.Turn.WHITE);

        board.performMove(firstA);
        /*
        possible_moves = board.generateMoves();
        for(Action a: possible_moves)
            System.out.println(a);
        */
        Action secondA = new Action("d9", "d6", State.Turn.BLACK);
        board.performMove(secondA);
        /*
        possible_moves = board.generateMoves();
        for(Action a: possible_moves)
            System.out.println(a);
        */
        Action thirdA = new Action("e7", "d7", State.Turn.WHITE);
        board.performMove(thirdA);

        /*
        possible_moves = board.generateMoves();
        for(Action a: possible_moves)
            System.out.println(a);
            */
    }

    public static void test(State.Pawn p){
        p = State.Pawn.WHITE;
    }
}
