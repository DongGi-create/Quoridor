package com.example.quoridor.domain;

public class User {
    private String tier = null;
    private int score = 0;
    private String id;
    private String email;
    private String name;
    private String pw;
    private History vs2_history;
    private History vs4_history;

    private static User instance;

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public History getVs2_history() {
        return vs2_history;
    }

    public void setVs2_history(History vs2_history) {
        this.vs2_history = vs2_history;
    }

    public History getVs4_history() {
        return vs4_history;
    }

    public void setVs4_history(History vs4_history) {
        this.vs4_history = vs4_history;
    }

    public User(String tier, int score, String id, String email, String name, String pw, History vs2_history, History vs4_history) {
        this.tier = tier;
        this.score = score;
        this.id = id;
        this.email = email;
        this.name = name;
        this.pw = pw;
        this.vs2_history = vs2_history;
        this.vs4_history = vs4_history;
    }
}
