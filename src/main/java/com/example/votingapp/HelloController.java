package com.example.votingapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class HelloController {
    @FXML
    public Label errorLabel;
    @FXML
    public Button loginButton, registerButton;
    @FXML
    public TextField unameField, passField;
    @FXML
    public TableView<Query> table;

    public void button1_Click(ActionEvent e) throws SQLException, IOException {
        Connection con = DriverManager.getConnection(HelloApplication.url, HelloApplication.uname, HelloApplication.password);
        PreparedStatement stmt = con.prepareStatement("SELECT COUNT(username) FROM Accounts WHERE Username = ? AND Password = ?;");
        stmt.setString(1, unameField.getText());
        stmt.setString(2, passField.getText());
        ResultSet set = stmt.executeQuery();
        set.next();
        if(set.getString(1).equals("1")){
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("VotingForm.fxml")));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Voting Form");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
        else{
            errorLabel.setText("Login credentials not valid");
            errorLabel.setTextFill(Color.RED);
            Message msg = new Message(errorLabel, 4000);
            msg.start();
        }
    }
}