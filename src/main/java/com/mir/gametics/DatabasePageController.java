package com.mir.gametics;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.sql.*;

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

    private TableColumn.CellEditEvent<Game, String> gameStringCellEditEvent;

    private GameModel gameModel;

    private Connection connection;

    @FXML
    public void initialize() {

        gameModel = new GameModel();

        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=gametics;user=sa;password=p@ssword81";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String selectQuery = "select * from GamesBasicInfo";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                gameModel.addGame(new Game(resultSet.getString(2), resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        initializeTable();
        editBtn.setVisible(false);
        deleteBtn.setVisible(false);

        editBtn.setOnAction(event -> {

            DialogBox.getDialogBox((Stage) editBtn.getScene().getWindow(), "You can now edit table");

            firstColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            firstColumn.setOnEditCommit(
                    gameStringCellEditEvent -> (gameStringCellEditEvent.getTableView().getItems().get(
                           gameStringCellEditEvent.getTablePosition().getRow())
                    ).setGameName(gameStringCellEditEvent.getNewValue())
            );


            secondColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            secondColumn.setOnEditCommit(
                    gameStringCellEditEvent -> (gameStringCellEditEvent.getTableView().getItems().get(
                            gameStringCellEditEvent.getTablePosition().getRow())
                    ).setGameCategory(gameStringCellEditEvent.getNewValue())

            );
        });

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

        tableView.setEditable(true);
        // Table Columns
        firstColumn = new TableColumn<>("Name");
        secondColumn = new TableColumn<>("Category");

        // Setting value type for column
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("gameCategory"));
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
