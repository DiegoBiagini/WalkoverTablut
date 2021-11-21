package com.walkover.tablut.client;

import com.walkover.tablut.domain.*;
import com.walkover.tablut.evaluator.WalkoverEvaluator;
import com.walkover.tablut.evaluator.WalkoverEvaluatorBlack;
import com.walkover.tablut.evaluator.WalkoverEvaluatorWhite;
import com.walkover.tablut.exceptions.ActionException;
import com.walkover.tablut.exceptions.SearchException;
import com.walkover.tablut.search.WalkoverSearch;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WalkoverClient extends TablutClient{

    public WalkoverClient(String player, String name, int timeout, String ipAddress) throws UnknownHostException, IOException {
        super(player, name, timeout, ipAddress);
    }

    public WalkoverClient(String player, int timeout, String ipAddress) throws UnknownHostException, IOException {
        this(player, "walkover", timeout, ipAddress);
    }

    public WalkoverClient(String player) throws UnknownHostException, IOException {
        this(player, "walkover", 60, "localhost");
    }


    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        String role = "";
        String name = "walkover";
        String ipAddress = "localhost";
        int timeout = 60;

        if (args.length < 1) {
            System.out.println("You must specify which player you are (WHITE or BLACK)");
            System.exit(-1);
        } else {
            System.out.println(args[0]);
            role = (args[0]);
        }
        if (args.length == 2) {
            System.out.println(args[1]);
            timeout = Integer.parseInt(args[1]);
        }
        if (args.length == 3) {
            ipAddress = args[2];
        }
        System.out.println("Selected client: " + args[0]);

        WalkoverClient client = new WalkoverClient(role, name, timeout, ipAddress);
        client.run();
    }

    @Override
    public void run() {
        try {
            declareName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ActiveBoard board = new ActiveBoard();

        System.out.println("You are player " + this.getPlayer().toString() + "!");

        while (true) {
            try {
                read();
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
                System.exit(1);
            }

            board.setGameState(getCurrentState());

            System.out.println("Current state:");
            System.out.println(board.getGameState().toString());

            State.Turn currentTurn = board.getTurn();


            if (currentTurn.equals(StateTablut.Turn.WHITEWIN)) {
                if(getPlayer() == StateTablut.Turn.WHITE)
                    System.out.println("YOU WIN!");
                else
                    System.out.println("YOU LOSE");
                System.exit(0);
            }
            else if (currentTurn.equals(StateTablut.Turn.BLACKWIN)) {
                if(getPlayer() == StateTablut.Turn.BLACK)
                    System.out.println("YOU WIN");
                else
                    System.out.println("YOU LOSE!");
                System.exit(0);
            }
            else if (currentTurn.equals(StateTablut.Turn.DRAW)) {
                System.out.println("DRAW!");
                System.exit(0);
            }
            else if (currentTurn.equals(getPlayer())) {
                Action chosenMove;
                chosenMove =  walkoverBehaviour(board, getTimeout() -1);

                System.out.println("Chosen move: " + chosenMove.toString());
                try {
                    this.write(chosenMove);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
            else if ( !currentTurn.equals(getPlayer())) {
                System.out.println("Waiting for your opponent's move... ");
            }
        }
    }

    private Action simpleRandomBehaviour(ActiveBoard board){
        ArrayList<Action> possibleMoves = board.generateMoves();
        int choice = ThreadLocalRandom.current().nextInt(0, possibleMoves.size());
        return possibleMoves.get(choice);
    }

    private Action walkoverBehaviour(ActiveBoard board, int timeoutS) {
        WalkoverEvaluator evaluator;
        if(board.getTurn() == State.Turn.BLACK)
            evaluator = new WalkoverEvaluatorBlack();
        else
            evaluator = new WalkoverEvaluatorWhite();

        WalkoverSearch search = new WalkoverSearch(board, evaluator);
        Thread searchThread = new Thread(search);
        searchThread.start();
        try {
            searchThread.join(timeoutS* 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        searchThread.interrupt();
        System.out.println(search.getResult());
        System.out.println("Nodes explored "  + search.nodesExplored);
        System.out.println("Depth reached:" + search.depthReached);

        if(search.getResult() == null) {
            System.out.println("Search was halted before reaching a solution");
            return simpleRandomBehaviour(board);
        }
        else
            return search.getResult();
    }
}
