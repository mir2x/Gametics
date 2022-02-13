package com.mir.gametics;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class ForgotPasswordPageController {

    private Connection connection;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button resetButton;

    public void initialize()
    {
        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Gametics;user=sa;password=p@ssword81";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resetButton.setOnAction(actionEvent -> {
          OnresetButton();
        });
    }

    private void OnresetButton() {
        String email = emailTextField.getText();
        String verifyMailQuery = "select * from Users where user_email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(verifyMailQuery);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                DialogBox.getDialogBox((Stage) emailTextField.getScene().getWindow(), "Email not found in the database. Please enter a valid email");
            }
            else {
                DialogBox.getDialogBox((Stage) emailTextField.getScene().getWindow(), "A recovery mail has sent to the address");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
