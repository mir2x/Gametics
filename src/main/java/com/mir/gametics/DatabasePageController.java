package com.mir.gametics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DatabasePageController {



    @FXML
    private TableView<Game> tableView;
    @FXML
    private TextField gameNameField;
    @FXML
    private TextField gameCategoryField;


    private TableColumn<Game, String> firstColumn;

    private TableColumn<Game, String> secondColumn;

    private GameModel gameModel;

    @FXML
    public void initialize() {
        firstColumn = new TableColumn<>("Name");
        secondColumn = new TableColumn<>("Category");

        firstColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("gameCategory"));

        gameModel = new GameModel();
        tableView.setItems(gameModel.getGameList());
        tableView.getColumns().addAll(firstColumn, secondColumn);
    }

    @FXML
    private void onAddGame() {
        String name = gameNameField.getText();
        String category = gameCategoryField.getText();
        gameModel.addGame(new Game(name, category));
    }
}
