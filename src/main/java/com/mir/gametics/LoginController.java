package com.mir.gametics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
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
    @FXML
    private Hyperlink createOneHyperLink;
    @FXML
    private Hyperlink forgotPasswordHyperLink;

    private Connection connection;

    @FXML
    public void initialize() {
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=gametics;user=sa;password=123";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createOneHyperLink.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("fxml/create-account-page.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) createOneHyperLink.getScene().getWindow();
            stage.setScene(scene);
        });

        forgotPasswordHyperLink.setOnAction(event -> {
                FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("fxml/forgot-pass-page.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) createOneHyperLink.getScene().getWindow();
                stage.setScene(scene);
        });
    }

    public void onLogin() {
        String userName = userField.getText();
        String userPassword = passwordField.getText();

        String sql_query = "select * from Users where user_name = ? and user_password = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPassword);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                DialogBox.getDialogBox((Stage) userField.getScene().getWindow(), "Wrong Username/Password");
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("fxml/database-page.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) userField.getScene().getWindow();
                stage.setScene(scene);

            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }
}
