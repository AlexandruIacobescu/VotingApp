package com.example.votingapp;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdministratorForm implements Initializable {

    @FXML
    public ComboBox<String> categoryComboBox;
    @FXML
    public TableColumn<Member, String> memIdCol, fnmCol, lnmCol, ssnCol, joinCol, catCol, emailCol;
    @FXML
    public TableView<Member> membersTableView;

    public void backButton_Click(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginForm.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Administrator Login");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void exitButton_Click() {
        Platform.exit();
    }

    public void refreshButton_Click() {

    }

    public void clearButton_Click() {

    }

    public void initTable() throws SQLException {
        ObservableList<Member> memList = FXCollections.observableArrayList();
        memIdCol.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        fnmCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lnmCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
        ssnCol.setCellValueFactory(new PropertyValueFactory<>("Ssn"));
        joinCol.setCellValueFactory(new PropertyValueFactory<>("JoinDate"));
        catCol.setCellValueFactory(new PropertyValueFactory<>("Category"));

        Connection conn = DriverManager.getConnection(HelloApplication.url, HelloApplication.uname, HelloApplication.password);
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery("SELECT * FROM members;");

        Member mem;
        while(res.next()){
            mem = new Member(res.getString("firstname"), res.getString("lastname"), res.getString("email"), res.getString("joined"), res.getString("memberid"), res.getString("ssn"), res.getString("category"));
            memList.add(mem);
        }
        membersTableView.setItems(memList);
        conn.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            initTable();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        categoryComboBox.getItems().addAll("Management", "Shareholder", "Board of directors");
    }
}
