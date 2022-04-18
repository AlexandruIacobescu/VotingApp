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
    public Button loginButton, registerButton, adminButton;
    @FXML
    public TextField unameField, passField;

    public void adminButton_Click(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdminLoginForm.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Administrator Login");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Metoda aferenta butonului de Login.
     * In momentul logarii, se confrunta datele din casuta de username si din cea de password cu ce se afla in DB,
     * Mai mult, se va retine si id-ul contului `accountID`, pentru a putea face legatura dintre account si member
     * Acasta legatura are scopul de a obtine din ce categorie face parte memebrul care s-a logat cu contul X.
     */
    public void button1_Click(ActionEvent e) throws SQLException, IOException {
        Connection con = DriverManager.getConnection(HelloApplication.url, HelloApplication.uname, HelloApplication.password);
        PreparedStatement stmt = con.prepareStatement("SELECT COUNT(username),accountID FROM Accounts WHERE BINARY Username = ? AND BINARY Password = ?;");
        stmt.setString(1, unameField.getText());
        stmt.setString(2, passField.getText());
        ResultSet set = stmt.executeQuery();
        set.next();
        if(set.getString(1).equals("1")){
            HelloApplication.accountid = set.getString(2); // se retine accounID-ul pentru a face o alta interogare de a obtine categoria membrului, penru a stii ce fel de intrebari vor trebui afisate in tableView-ul din urmatoarea Forma
            HelloApplication.ausername = unameField.getText();
            HelloApplication.apassword = passField.getText();
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
    public void registerButton_Click(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("RegisterForm.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Register Form");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}