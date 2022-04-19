package com.example.votingapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.util.Objects;

public class RegisterForm {

    @FXML
    public TextField unameField, passField, cpassField, midField;

    public void showErrorDialog(String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Register");
        alert.setHeaderText("Error creating member account");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showExceptionDialog(SQLException ex){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Look, an Exception Dialog");
        alert.setContentText("Could not find file blabla.txt!");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    public void createButton_Click() throws SQLException {
        boolean ok = true;
        if(unameField.getText().equals("") || passField.getText().equals("") || cpassField.getText().equals("") || midField.getText().equals("")){
            showErrorDialog("There are empty fields left.");
            ok = false;
        }
        else if(!passField.getText().equals(cpassField.getText()) && ok){
            showErrorDialog("The passwords do not match.");
            passField.setText("");
            cpassField.setText("");
        }
        else if (ok){
            Connection conn = DriverManager.getConnection(HelloApplication.url, HelloApplication.uname, HelloApplication.password);
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(accountID) FROM accounts WHERE accountID = ?;");
            stmt.setString(1, midField.getText());
            ResultSet res = stmt.executeQuery();
            res.next();
            if(res.getString(1).equals("1")){
                showErrorDialog("This member already has an account.");
                ok = false;
            }
            conn.close();
        }
        if(ok){
            Connection conn = DriverManager.getConnection(HelloApplication.url, HelloApplication.uname, HelloApplication.password);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO accounts(accountID, username, password) VALUES(?,?,?);");
            stmt.setString(1, midField.getText());
            stmt.setString(2, unameField.getText());
            stmt.setString(3, passField.getText());
            try{
                stmt.executeUpdate();
            }
            catch (SQLException e){
                ok = false;

            }
        }
    }

    public void backButton_Click(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginForm.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
