package com.example.quoridor.domain;

import android.util.Pair;

import java.util.ArrayList;

public class Board {
    // 현재상태: 사용자별 - (벽 남은 개수, 남은 시간, 말 위치, uid, 색깔), 벽 세워진 위치, 2인/4인,
    Player[] player = new Player[4];
    ArrayList<Pair<Integer, Integer>> horizontalWalls;
    ArrayList<Pair<Integer, Integer>> verticalWalls;
    boolean is_1v1;

    public Board(Player[] player, ArrayList<Pair<Integer, Integer>> horizontalWalls, ArrayList<Pair<Integer, Integer>> verticalWalls, boolean is_1v1) {
        this.player = player;
        this.horizontalWalls = horizontalWalls;
        this.verticalWalls = verticalWalls;
        this.is_1v1 = is_1v1;
    }

    public Player[] getPlayer() {
        return player;
    }

    public void setPlayer(Player[] player) {
        this.player = player;
    }

    public ArrayList<Pair<Integer, Integer>> getHorizontalWalls() {
        return horizontalWalls;
    }

    public void setHorizontalWalls(ArrayList<Pair<Integer, Integer>> horizontalWalls) {
        this.horizontalWalls = horizontalWalls;
    }

    public ArrayList<Pair<Integer, Integer>> getVerticalWalls() {
        return verticalWalls;
    }

    public void setVerticalWalls(ArrayList<Pair<Integer, Integer>> verticalWalls) {
        this.verticalWalls = verticalWalls;
    }

    public boolean isIs_1v1() {
        return is_1v1;
    }

    public void setIs_1v1(boolean is_1v1) {
        this.is_1v1 = is_1v1;
    }
}
