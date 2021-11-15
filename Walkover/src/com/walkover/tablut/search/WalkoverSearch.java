package com.walkover.tablut.search;

import com.walkover.tablut.domain.Action;
import com.walkover.tablut.domain.ActiveBoard;
import com.walkover.tablut.domain.State;

import java.io.IOException;

public class WalkoverSearch implements Runnable {
    private ActiveBoard board;
    private Action actionFound;

    public WalkoverSearch(ActiveBoard board){
        this.board = board;
        this.actionFound = null;
    }

    // TODO:
    public void minimax(){}

    @Override
    public void run(){

    }

    public Action getResult(){
        return actionFound;
    }
}
