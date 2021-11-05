package com.walkover.tablut.client;

import java.io.IOException;
import java.net.UnknownHostException;

public class WalkoverClient extends TablutClient{


    public WalkoverClient(String player, String name, int timeout, String ipAddress) throws UnknownHostException, IOException {
        super(player, name, timeout, ipAddress);
    }

    public WalkoverClient(String player, String name, int timeout) throws UnknownHostException, IOException {
        super(player, name, timeout);
    }

    public WalkoverClient(String player, String name) throws UnknownHostException, IOException {
        super(player, name);
    }

    public WalkoverClient(String player, String name, String ipAddress) throws UnknownHostException, IOException {
        super(player, name, ipAddress);
    }

    @Override
    public void run() {

    }

    public static void main(String[] args){

    }
}