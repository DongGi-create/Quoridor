package com.example.quoridor.domain;

import com.example.quoridor.domain.utils.PlayerColorType;

public class Player {
    // (벽 남은 개수, 남은 시간, 말 위치, uid, 색깔)
    int left_wall;
    long left_ms;
    int row;
    int col;
    String uid;
    PlayerColorType playerColorType;

    public int getLeft_wall() {
        return left_wall;
    }

    public Player(int left_wall, long left_ms, int row, int col, String uid, PlayerColorType playerColorType) {
        this.left_wall = left_wall;
        this.left_ms = left_ms;
        this.row = row;
        this.col = col;
        this.uid = uid;
        this.playerColorType = playerColorType;
    }

    public void setLeft_wall(int left_wall) {
        this.left_wall = left_wall;
    }

    public long getLeft_ms() {
        return left_ms;
    }

    public void setLeft_ms(long left_ms) {
        this.left_ms = left_ms;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public PlayerColorType getPlayerColorType() {
        return playerColorType;
    }

    public void setPlayerColorType(PlayerColorType playerColorType) {
        this.playerColorType = playerColorType;
    }
}
