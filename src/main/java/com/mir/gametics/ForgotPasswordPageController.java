package com.mir.gametics;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ForgotPasswordPageController {

    private Connection connection;

    @FXML
    private Button resetButton;

    public void initialize()
    {
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Gametics;user=sa;password=123";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resetButton.setOnAction(actionEvent -> {
          OnresetButton();
        });
    }

    private void OnresetButton() {
    }


}
