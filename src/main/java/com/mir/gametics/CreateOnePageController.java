package com.mir.gametics;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateOnePageController {

    private Connection connection;

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField userEmailTextField;

    @FXML
    private PasswordField userPasswordField;

    @FXML
    private Hyperlink loginPageHyperLink;

    @FXML
    public void initialize()
    {
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=gametics;user=sa;password=p@ssword81";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        createAccountButton.setOnAction(actionEvent -> {
            onCreateAccount();
            try {
                backToLoginPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        loginPageHyperLink.setOnAction(actionEvent -> {
            try {
                backToLoginPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    private void onCreateAccount() {
        String userName = userNameTextField.getText();
        String userEmail = userEmailTextField.getText();
        String userPassword = userPasswordField.getText();

        String insertQuery = "INSERT INTO Users VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userEmail);
            preparedStatement.setString(3, userPassword);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void backToLoginPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Gametics.class.getResource("fxml/login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) createAccountButton.getScene().getWindow();
        stage.setScene(scene);
    }
}
