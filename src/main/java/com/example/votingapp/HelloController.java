package com.example.votingapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.*;

public class HelloController {
    @FXML
    public Label errorLabel;
    @FXML
    public Button loginButton, registerButton;
    @FXML
    public TextField unameField, passField;

    @FXML
    public void button1_Click() throws SQLException{
        Connection con = DriverManager.getConnection(HelloApplication.url, HelloApplication.uname, HelloApplication.password);
        PreparedStatement stmt = con.prepareStatement("SELECT COUNT(username) FROM Accounts WHERE Username = ? AND Password = ?;");
        stmt.setString(1, unameField.getText());
        stmt.setString(2, passField.getText());
        ResultSet set = stmt.executeQuery();
        set.next();
        if(set.getString(1).equals("1")){

        }
        else{
            errorLabel.setText("Login credentials not valid");
            errorLabel.setTextFill(Color.RED);
            Message msg = new Message(errorLabel, 4000);
            msg.start();
        }
    }
}