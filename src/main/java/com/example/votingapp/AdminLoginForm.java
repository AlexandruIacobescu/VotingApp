package com.example.votingapp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AdminLoginForm {

    @FXML
    public Button loginButton, backButton, exitButton;
    @FXML
    public TextField unameTextField, passTextField;
    @FXML
    public Label errorLabel;

    public void loginButton_CLick(ActionEvent e) throws IOException {
        if(unameTextField.getText().equals("overboss") && passTextField.getText().equals("admin")){
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdministratorForm.fxml")));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Administrator Login");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
        else{
            Message msg = new Message(errorLabel, 4000);
            msg.start();
        }
    }

    public void backButton_CLick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginForm.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Administrator Login");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void exitButton_CLick() {
        Platform.exit();
    }
}
