package com.example.quoridor.domain;

import com.example.quoridor.domain.utils.NotationType;

public class Notation {
    // 기보: 가로벽/세로벽/말, to row, to col
    NotationType type;
    int row;
    int col;

    public Notation(NotationType type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    public NotationType getType() {
        return type;
    }

    public void setType(NotationType type) {
        this.type = type;
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
}
