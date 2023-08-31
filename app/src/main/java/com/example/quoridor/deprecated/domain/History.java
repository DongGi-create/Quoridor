package com.example.quoridor.deprecated.domain;

import java.util.ArrayList;

public class History {
    private int gamecnt;
    private int wincnt;
    private ArrayList<String> gameids;

    public int getGamecnt() {
        return gamecnt;
    }

    public void setGamecnt(int gamecnt) {
        this.gamecnt = gamecnt;
    }

    public int getWincnt() {
        return wincnt;
    }

    public void setWincnt(int wincnt) {
        this.wincnt = wincnt;
    }

    public ArrayList<String> getGameids() {
        return gameids;
    }

    public void setGameids(ArrayList<String> gameids) {
        this.gameids = gameids;
    }

    public History(int gamecnt, int wincnt, ArrayList<String> gameids) {
        this.gamecnt = gamecnt;
        this.wincnt = wincnt;
        this.gameids = gameids;
    }
}