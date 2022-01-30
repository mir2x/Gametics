package com.mir.gametics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class LoginController {

    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;

    private Connection connection;

    @FXML
    public void initialize() {
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=gametics;user=sa;password=p@ssword81";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onLogin() {
        String userName = userField.getText();
        String userPassword = passwordField.getText();

        String sql_query = "select * from users where username = ? and password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPassword);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                DialogBox.getDialogBox((Stage) userField.getScene().getWindow(), "Wrong Username/Password");
            } else {
                System.out.println("right");
                FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("database-page.fxml"));
                Scene scene = userField.getScene();
                scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) userField.getScene().getWindow();
                stage.setScene(scene);

            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }
}
