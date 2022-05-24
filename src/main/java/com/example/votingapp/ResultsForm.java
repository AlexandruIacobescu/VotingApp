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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class ResultsForm implements Initializable {

    @FXML
    TableView<Query> table;
    @FXML
    TableColumn<Query, Integer> queryIdCol, approveCol, disapproveCol;
    @FXML
    TableColumn<Query, String> accessCol, textCol;
    @FXML
    Button backButton, exitButton;
    @FXML
    ComboBox<String> queryComboBox;
    @FXML
    TextArea queryTextArea;

    public Map<Integer, String> map = new HashMap<>();

    public void initTable() throws SQLException {
        Connection conn = DriverManager.getConnection(HelloApplication.url, HelloApplication.uname, HelloApplication.password);

        PreparedStatement st = conn.prepareStatement("SELECT * FROM queries WHERE queryID IN (SELECT queryID FROM votes WHERE voterID = ?);");
        st.setString(1, HelloApplication.accountid);
        ResultSet set = st.executeQuery();

        ObservableList<Query> queries = FXCollections.observableArrayList();
        queryIdCol.setCellValueFactory(new PropertyValueFactory<>("QueryId"));
        textCol.setCellValueFactory(new PropertyValueFactory<>("Text"));
        accessCol.setCellValueFactory(new PropertyValueFactory<>("AccessLevel"));
        approveCol.setCellValueFactory(new PropertyValueFactory<>("Approve"));
        disapproveCol.setCellValueFactory(new PropertyValueFactory<>("Disapprove"));
        while(set.next()){
            queries.add(new Query(set.getInt(1),set.getString(2),set.getString(3),set.getInt(4),set.getInt(5)));
            queryComboBox.getItems().add(set.getString(1));
            map.put(set.getInt(1), set.getString(2));
        }
        table.setItems(queries);
        conn.close();
    }
    public void exitButton_Click(){
        Platform.exit();
    }
    public void backButton_Click(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("VotingForm.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Voting Form");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    public void queryComboBox_IndexChanged(){
        queryTextArea.setText(map.get(Integer.parseInt(queryComboBox.getValue())));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            initTable();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }
}
