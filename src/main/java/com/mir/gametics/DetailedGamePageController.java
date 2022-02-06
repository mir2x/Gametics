package com.mir.gametics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.sql.*;

public class DetailedGamePageController {

    @FXML
    private TextField nameField;

    @FXML
    private Label nameLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private ImageView imageView;


    private Connection connection;


    @FXML
    public void initialize() {

        GameHolder gameHolder = GameHolder.getInstance();

        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=gametics;user=sa;password=p@ssword81";

        try {
            connection = DriverManager.getConnection(connectionUrl);
            System.out.println("successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        nameLabel.setText(gameHolder.getName());
        String sqlQuery = "select GamesBasicInfo.game_name, Category.category_name, GamesBasicInfo.game_release_date, GamesBasicInfo.game_price, GamePicture.picture\n" +
                "from GamesBasicInfo\n" +
                "         INNER JOIN GameCategory on GamesBasicInfo.game_id = GameCategory.game_id\n" +
                "         INNER JOIN Category on Category.category_id = GameCategory.category_id\n" +
                "         INNER JOIN GamePicture on GamesBasicInfo.game_id = GamePicture.game_id\n" +
                "where GamesBasicInfo.game_name = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, gameHolder.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                nameLabel.setText(resultSet.getString(1));
                categoryLabel.setText(resultSet.getString(2));
                InputStream inputStream = resultSet.getBinaryStream("picture");
                imageView.setImage(new Image(inputStream));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
