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
    @FXML
    private Button editBtn;
    @FXML
    private Button deleteBtn;


    private TableColumn<Game, String> firstColumn;

    private TableColumn<Game, String> secondColumn;

    private GameModel gameModel;

    @FXML
    public void initialize() {

        initializeTable();

        editBtn.setVisible(false);
        deleteBtn.setVisible(false);
        deleteBtn.setOnAction(event -> {
            Game selectedGame = tableView.getSelectionModel().getSelectedItem();
            tableView.getItems().remove(selectedGame);
        });

        tableView.getSelectionModel().selectedItemProperty().addListener(((observableValue, game, t1) -> {
            if(t1 != null) {
                editBtn.setVisible(true);
                deleteBtn.setVisible(true);
            }
        }));
    }

    public void initializeTable() {
        // Table Columns
        firstColumn = new TableColumn<>("Name");
        secondColumn = new TableColumn<>("Category");

        // Setting value type for column
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("gameCategory"));

        gameModel = new GameModel();

        // setting item on table
        tableView.setItems(gameModel.getGameList());
        // adding column to table
        tableView.getColumns().addAll(firstColumn, secondColumn);
    }

    @FXML
    private void onAddGame() {
        String name = gameNameField.getText();
        String category = gameCategoryField.getText();
        gameModel.addGame(new Game(name, category));
    }


}