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

/**
 * Aceasta clasa controller aferenta Formei de Votare (VotingForm.fxml), implementeaza interfata `Initializable`
 * Interafata aceasta da @Override la metoda `initialize` (vezi mai jos)
 * In interiorul acestei metode se pun toate lucrurile ce trebuie sa se intample in momentul
 * in care se deschide o scena (eg. cand se deschide scena de vot, tabelul trebuie populat cu date din DB.)
 * => se pune metoda `initTable`, care populeaza tabelul, in metoda `initialize`.
 */
public class VotingForm implements Initializable {
    /**
     * REMINDER!!!
     * Markerul `@FXML` care se pune inaintea declararii unor variablie de tip obiect (din Stage)
     * marcheaza faptul ca vreau sa manipulez in acest controller obiecte din Stage (facute cu Scene Builder)
     * Toate setarile si modificarile facute pe Stage in SceneBuilder, se reflecta in fisierul .fxml aferent.
     * DONNOT forget to save after any changes done in the Scene Builder.
     */
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

    // Map-ul `textColMap` pune in corespondenta queryid-ul din DB (i.e. tabelul `queries`) cu
    // textul aferent acelei intrebari, se foloseste pentru a putea asocia un interiorul programului
    // queryid-ul cu textul intrebarii ca sa se poata afisa in casuta text mai jos de combo box
    // pentru a putea vedea toata intrebarea in momentul in care se schimba elemetul selectat in combo-box. Vezi metoda `queryComboBox_IndexChanged()`
    public  Map<Integer, String> textColMap= new HashMap<>();

    public void reloadMap(ResultSet res) throws SQLException {
        textColMap.put(Integer.parseInt(res.getString(1)), res.getString(2));
    }

    /**
     * Aceasta metoda determina prin intermediul unei interogari in DB, toate intrbarile
     * ce trebuie afisate la deschiderea formei de votare, folosindu-se de memeber-id-ul (== accountID)
     * Determinat la momentul logarii, si pus in variablia statica HelloApplication.accountid
     */
    public String getMemberType(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement("SELECT category FROM members WHERE memberid = ?;");
        stmt.setString(1, HelloApplication.accountid);
        ResultSet set = stmt.executeQuery();
        set.next();
        return set.getString(1);
    }

    // Metoda `initTable` populeaza tabelul cu datele din DB
    public void initTable() throws SQLException {
        queryComboBox.getItems().removeAll();  // se golesc toate elementele rezisuale din combo box, potential ramase la logarea anterioara
        idCol.setCellValueFactory(new PropertyValueFactory<>("QueryId")); // se initializeaza coloanele din tableView
        textCol.setCellValueFactory(new PropertyValueFactory<>("Text")); // denumirea din paranteza trebuie sa fie aceeasi cu numele getterului
                                                                            // aferent din obiectul care modleaza ce intra in tabel, minus prefixul `get-`
        ObservableList<Query> queries = FXCollections.observableArrayList();  // lista in care se vor baga obiectele ce intra in tableview, dupa executia interogarii din DB mai jos
        Connection conn = DriverManager.getConnection(HelloApplication.url, HelloApplication.uname, HelloApplication.password);  // se realizeaza conexiunea cu DB
        PreparedStatement st = conn.prepareStatement("SELECT queryid,text FROM queries WHERE accesslevel = ?;");  // din tabelul `queries` se extrage id-ul si textul aferent
        st.setString(1, getMemberType(conn));
        ResultSet res = st.executeQuery();
        while(res.next()){
            queries.add(new Query(Integer.parseInt(res.getString(1)), res.getString(2)));  // se adauga in lista declarat mai sus, ce se extrage din DB
            queryComboBox.getItems().add(res.getString(1));  // se populeaza combo-box-ul cu queryid-urile fiecarei intrebari din tabel
            reloadMap(res);  // se reactulizeaza map-ul
        }
        table.setItems(queries);  // se introduc din lista obiectele in tabel
        conn.close();  // se inchide conexiunea cu DB
    }

    // Aceasta metoda se executa la EVENIMENTUL : "Schimbare element selectat de catre utiliztor in combo-box"
    // Are scopul de a reactualiza textul intrebarii afisate in casuta text de vizualizare a intregii intrebari
    // in momentul in care selectez o alta intrebare din combo-box
    public void queryComboBox_IndexChanged(){
        // Aici se face util map-ul initializat la inceputul controller-ului
        textArea.setText(textColMap.get(Integer.parseInt(queryComboBox.getValue())));
    }

    // Aceasta este metoda care inchide stage-ul actual si redeschide stage-ul de logare
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
