package com.mir.gametics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GameModel {

    ObservableList<Game> gameList = FXCollections.observableArrayList(
            new Game("Call of Duty 4", "Action"),
            new Game("Fifa 21", "Sports")
    );

    public ObservableList<Game> getGameList() {
        return gameList;
    }

    public void setGameList(ObservableList<Game> gameList) {
        this.gameList = gameList;
    }

    public void addGame(Game game) {
        gameList.add(game);
    }
}
