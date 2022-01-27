package com.mir.gametics;

public class Game {
    private String gameName;
    private String gameCategory;

    public Game() {
    }

    public Game(String gameName, String gameCategory) {
        this.gameName = gameName;
        this.gameCategory = gameCategory;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameCategory() {
        return gameCategory;
    }

    public void setGameCategory(String gameCategory) {
        this.gameCategory = gameCategory;
    }
}
