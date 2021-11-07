package com.walkover.tablut.client;

import com.walkover.tablut.domain.*;

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

        State state = new StateTablut();
        GameAshtonTablut rules = new GameAshtonTablut(99, 0, "garbage", "fake", "fake");
        state.setTurn(State.Turn.WHITE);

        ActiveBoard board = new ActiveBoard(state, rules);

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
                    System.out.println("YOU WIN");
                System.exit(0);
            }
            else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
                if(getPlayer() == StateTablut.Turn.BLACK)
                    System.out.println("YOU LOSE");
                else
                    System.out.println("YOU WIN!");
                System.exit(0);
            }
            else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
                System.out.println("DRAW!");
                System.exit(0);
            }
            else if (currentTurn.equals(getPlayer())) {
                // TODO: my turn
                // Generate moves
                // Start minimax/negamax
                ArrayList<Action> possibleMoves = board.generateMoves();
                int choice = ThreadLocalRandom.current().nextInt(0, possibleMoves.size());
                Action chosenMove = possibleMoves.get(choice);

                System.out.println("Mossa scelta: " + chosenMove.toString());
                try {
                    this.write(chosenMove);
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
            else if ( !currentTurn.equals(getPlayer())) {
                System.out.println("Waiting for your opponent move... ");
            }
        }
    }
}
