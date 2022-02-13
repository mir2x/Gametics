package com.mir.gametics;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DatabasePageController {

    @FXML
    private TableView<Game> tableView;

    @FXML
    private TextField gameNameTextField;
    @FXML
    private TextField gameCategoryTextField;
    @FXML
    private TextField gameReleaseDateTextField;
    @FXML
    private TextField gamePriceTextField;
    @FXML
    private TextField searchTextField;

    @FXML
    private Button deleteButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button addGameButton;

    @FXML
    private CheckBox exactMatchingCheckBox;

    @FXML
    private Hyperlink advancedSearchHyperLink;


    private TableColumn<Game, String> firstColumn;
    private TableColumn<Game, String> secondColumn;
    private TableColumn<Game, String> thirdColumn;
    private TableColumn<Game, String> fourthColumn;

    private TableColumn.CellEditEvent<Game, String> gameStringCellEditEvent;

    private GameModel gameModel;

    private GameHolder gameHolder;

    private Connection connection;

    private Scene scene;

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

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
            preparedStatement = connection.prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                gameModel.addGame(new Game(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        initializeTable();

//        editButton.setOnAction(event -> {
//
//            DialogBox.getDialogBox((Stage) editButton.getScene().getWindow(), "You can now edit table");
//
//            firstColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//            firstColumn.setOnEditCommit(
//                    gameStringCellEditEvent -> (gameStringCellEditEvent.getTableView().getItems().get(
//                            gameStringCellEditEvent.getTablePosition().getRow())
//                    ).setGameName(gameStringCellEditEvent.getNewValue())
//            );
//
//
//            secondColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//            secondColumn.setOnEditCommit(
//                    gameStringCellEditEvent -> (gameStringCellEditEvent.getTableView().getItems().get(
//                            gameStringCellEditEvent.getTablePosition().getRow())
//                    ).setGameCategory(gameStringCellEditEvent.getNewValue())
//
//            );
//        });

        deleteButton.setOnAction(event -> {
            Game selectedGame = tableView.getSelectionModel().getSelectedItem();
            tableView.getItems().remove(selectedGame);
        });

        tableView.setOnMousePressed(mouseEvent -> {
                if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {

                    gameHolder.setName(tableView.getSelectionModel().getSelectedItem().getGameName());
                    FXMLLoader fxmlLoader = new FXMLLoader(DatabasePageController.class.getResource("fxml/detailed-game-page.fxml"));

                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Stage stage = (Stage) gameNameTextField.getScene().getWindow();
                    stage.setScene(scene);
                }
        });
        searchButton.setOnAction(event -> {
            searchGame();
        });

        addGameButton.setOnAction(event -> {
            addGame();
        });

        deleteButton.setOnAction(event -> {
            deleteGame();
        });
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


    
    private void searchGame() {
        gameModel.clearList();
        String searchedGame = searchTextField.getText();
        if(exactMatchingCheckBox.isSelected()) {
            String matchQuery = "select GamesBasicInfo.game_name, Category.category_name, GamesBasicInfo.game_release_date, GamesBasicInfo.game_price\n" +
                    "from GamesBasicInfo\n" +
                    "         INNER JOIN GameCategory on GamesBasicInfo.game_id = GameCategory.game_id\n" +
                    "         INNER JOIN Category on Category.category_id = GameCategory.category_id\n" +
                    "where game_name=?";
            try {
                preparedStatement = connection.prepareStatement(matchQuery);
                preparedStatement.setString(1, searchedGame);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    gameModel.addGame(new Game(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        else {
            String matchSimilarQuery = "select GamesBasicInfo.game_name, Category.category_name, GamesBasicInfo.game_release_date, GamesBasicInfo.game_price\n" +
                    "from GamesBasicInfo\n" +
                    "         INNER JOIN GameCategory on GamesBasicInfo.game_id = GameCategory.game_id\n" +
                    "         INNER JOIN Category on Category.category_id = GameCategory.category_id\n" +
                    "where game_name like ?";
            try {
                preparedStatement = connection.prepareStatement(matchSimilarQuery);
                preparedStatement.setString(1,"%" + searchedGame + "%");
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    gameModel.addGame(new Game(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addGame() {
        String gameId = null;
        String name = gameNameTextField.getText();
        String category = gameCategoryTextField.getText();
        String categoryId = null;
        String releaseDate = gameReleaseDateTextField.getText();
        String price = gamePriceTextField.getText();
        gameModel.addGame(new Game(name, category, releaseDate, price));
        String insertQuery = "insert into GamesBasicInfo\n" +
                "values(?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, releaseDate);
            preparedStatement.setString(3, price);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String findGameId = "select game_id\n" +
                "from GamesBasicInfo\n" +
                "where game_name = ?";
        String findCategoryId = "select *\n" +
                "from Category\n" +
                "where category_name = ?";
        String completeInsertQuery = "insert into GameCategory\n" +
                "values(?,?)";
        ;        try {
            preparedStatement = connection.prepareStatement(findGameId);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                gameId = resultSet.getString(1);
                System.out.println(gameId);
            }
            preparedStatement = connection.prepareStatement(findCategoryId);
            preparedStatement.setString(1, category);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categoryId = resultSet.getString(1);
                System.out.println(categoryId);
            }
            preparedStatement = connection.prepareStatement(completeInsertQuery);
            preparedStatement.setString(1, gameId);
            preparedStatement.setString(2, categoryId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void deleteGame() {
        Game selectedGames = tableView.getSelectionModel().getSelectedItem();
        if(selectedGames == null) {
            DialogBox.getDialogBox((Stage) tableView.getScene().getWindow(), "First Select A Game To Delete");
        }
        else {
            String name = selectedGames.getGameName();
            String gameId = null;

            String findGameId = "select game_id\n" +
                    "from GamesBasicInfo\n" +
                    "where game_name = ?";

            String removeCategoryQuery = "delete from GameCategory where game_id = ?";
            String removeGameQuery = "delete from GamesBasicInfo where game_id = ?";

            try {
                preparedStatement = connection.prepareStatement(findGameId);
                preparedStatement.setString(1, name);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    gameId = resultSet.getString(1);
                    System.out.println(gameId);
                }

                preparedStatement = connection.prepareStatement(removeCategoryQuery);
                preparedStatement.setString(1, gameId);
                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement(removeGameQuery);
                preparedStatement.setString(1, gameId);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }


            tableView.getItems().remove(selectedGames);
        }

    }



}
