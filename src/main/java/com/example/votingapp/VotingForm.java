package com.example.votingapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class VotingForm implements Initializable {
    @FXML
    public TableView<Query> table;
    @FXML
    public TableColumn<Query, Integer> idCol;
    @FXML
    public TableColumn<Query, String> textCol;
    @FXML
    public ComboBox<String> queryComboBox;
    @FXML
    public TextArea textArea;
    private Map<Integer, String> textColMap = new HashMap<>();
    public void initTable() throws SQLException {
        idCol.setCellValueFactory(new PropertyValueFactory<>("QueryId"));
        textCol.setCellValueFactory(new PropertyValueFactory<>("Text"));
        ObservableList<Query> queries = FXCollections.observableArrayList();
        Connection conn = DriverManager.getConnection(HelloApplication.url, HelloApplication.uname, HelloApplication.password);
        Statement st = conn.createStatement();
        ResultSet res = st.executeQuery("SELECT queryid,text FROM queries;");
        while(res.next()){
            queries.add(new Query(Integer.parseInt(res.getString(1)), res.getString(2)));
            queryComboBox.getItems().add(res.getString(1));
            textColMap.put(Integer.parseInt(res.getString(1)), res.getString(2));
        }
        table.setItems(queries);
        conn.close();
    }

    public void queryComboBox_IndexChanged(){
        textArea.setWrapText(true);
        textArea.setText(textColMap.get(Integer.parseInt(queryComboBox.getValue())));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
