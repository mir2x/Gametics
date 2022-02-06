package com.mir.gametics;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
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
    private TableColumn<Game, String> thirdColumn;
    private TableColumn<Game, String> fourthColumn;

    private TableColumn.CellEditEvent<Game, String> gameStringCellEditEvent;

    private GameModel gameModel;

    private GameHolder gameHolder;

    private Connection connection;

    private Scene scene;

    @FXML
    public void initialize() {

        gameModel = new GameModel();
        gameHolder = GameHolder.getInstance();

        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=gametics;user=sa;password=p@ssword81";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

       String selectQuery = "select GamesBasicInfo.game_name, Category.category_name, GamesBasicInfo.game_release_date, GamesBasicInfo.game_price\n" +
               "from GamesBasicInfo\n" +
               "INNER JOIN GameCategory on GamesBasicInfo.game_id = GameCategory.game_id\n" +
               "inner join Category on Category.category_id = GameCategory.category_id";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                gameModel.addGame(new Game(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
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

        tableView.setOnMousePressed(mouseEvent -> {
                if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
                    System.out.println(tableView.getSelectionModel().getSelectedItem().getGameName());
                    gameHolder.setName(tableView.getSelectionModel().getSelectedItem().getGameName());
                    FXMLLoader fxmlLoader = new FXMLLoader(DatabasePageController.class.getResource("detailed-game-page.fxml"));

                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Stage stage = (Stage) gameNameField.getScene().getWindow();
                    stage.setScene(scene);
                }
        });

//        tableView.getSelectionModel().selectedItemProperty().addListener(((observableValue, game, t1) -> {
//            if(t1 != null) {
//
//                gameHolder.setName(t1.getGameName());
//                FXMLLoader fxmlLoader = new FXMLLoader(DatabasePageController.class.getResource("detailed-game-page.fxml"));
//
//                try {
//                    scene = new Scene(fxmlLoader.load());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                Stage stage = (Stage) gameNameField.getScene().getWindow();
//                stage.setScene(scene);
//            }
//        }));
    }

    public void initializeTable() {

        tableView.setEditable(true);
        // Table Columns
        firstColumn = new TableColumn<>("Name");
        secondColumn = new TableColumn<>("Category");
        thirdColumn = new TableColumn<>("Release Date");
        fourthColumn = new TableColumn<>("Price");

        // Setting value type for column
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("gameCategory"));
        thirdColumn.setCellValueFactory(new PropertyValueFactory<>("gameReleaseDate"));
        fourthColumn.setCellValueFactory(new PropertyValueFactory<>("gamePrice"));
        // setting item on table
        tableView.setItems(gameModel.getGameList());
        // adding column to table
        tableView.getColumns().addAll(firstColumn, secondColumn, thirdColumn, fourthColumn);
    }

    @FXML
    private void onAddGame() {
//        String name = gameNameField.getText();
//        String category = gameCategoryField.getText();
//        gameModel.addGame(new Game(name, category));
    }


}
