package com.example.votingapp;

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
    @FXML
    public Button backButton, approveButton, disapproveButton;

    public  Map<Integer, String> textColMap= new HashMap<>();

    public void reloadMap(ResultSet res) throws SQLException {
        textColMap.put(Integer.parseInt(res.getString(1)), res.getString(2));
    }
    public String getMemberType(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("SELECT category FROM members WHERE memberid = ?;");
        stmt.setString(1, HelloApplication.accountid);
        ResultSet set = stmt.executeQuery();
        set.next();
        return set.getString(1);
    }
    public void initTable() throws SQLException {
        queryComboBox.getItems().removeAll();
        idCol.setCellValueFactory(new PropertyValueFactory<>("QueryId"));
        textCol.setCellValueFactory(new PropertyValueFactory<>("Text"));
        ObservableList<Query> queries = FXCollections.observableArrayList();
        Connection conn = DriverManager.getConnection(HelloApplication.url, HelloApplication.uname, HelloApplication.password);
        PreparedStatement st = conn.prepareStatement("SELECT queryid,text FROM queries WHERE accesslevel = ?;");
        st.setString(1, getMemberType(conn));
        ResultSet res = st.executeQuery();
        while(res.next()){
            queries.add(new Query(Integer.parseInt(res.getString(1)), res.getString(2)));
            queryComboBox.getItems().add(res.getString(1));
            reloadMap(res);
        }
        table.setItems(queries);
        conn.close();
    }

    public void queryComboBox_IndexChanged(){
        textArea.setText(textColMap.get(Integer.parseInt(queryComboBox.getValue())));
        int i = 0;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
