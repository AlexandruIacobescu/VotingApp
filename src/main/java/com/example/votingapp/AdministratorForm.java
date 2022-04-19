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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
    @FXML
    public Button insertButton;
    @FXML
    public TextField fnmField, lnmField, ssnField, emailField, memberField;
    @FXML
    public DatePicker joinDatePicker;

    public void backButton_Click(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginForm.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Administrator Login");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void insertButton_Click(){
        try{
            Connection conn = DriverManager.getConnection(HelloApplication.url, HelloApplication.uname, HelloApplication.password);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Members(memberID,firstname,lastname,ssn,joined,email,category) VALUES(?,?,?,?,?,?,?);");
            if(!memberField.getText().equals(""))
                stmt.setString(1, memberField.getText());
            if(!fnmField.getText().equals(""))
                stmt.setString(2, fnmField.getText());
            if(!lnmField.getText().equals(""))
                stmt.setString(3, lnmField.getText());
            if(!ssnField.getText().equals(""))
                stmt.setString(4, ssnField.getText());
            stmt.setDate(5, Date.valueOf(joinDatePicker.getValue()));
            if(!emailField.getText().equals(""))
                stmt.setString(6, emailField.getText());
            if(categoryComboBox.getValue() != null)
                stmt.setString(7, categoryComboBox.getValue());
            stmt.executeUpdate();
            initTable();
            conn.close();
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Insertion Failed");
            alert.setHeaderText("Member insertion failed");
            alert.setContentText("Wrong text fields format");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.write(ex.toString());
            String exceptionText = sw.toString();

            Label label = new Label("The exception was:");

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
    }

    public void exitButton_Click() {
        Platform.exit();
    }

    public void refreshButton_Click() throws SQLException {
        initTable();
    }

    public void clearButton_Click() {
        fnmField.setText("");
        lnmField.setText("");
        ssnField.setText("");
        emailField.setText("");
        memberField.setText("");
        categoryComboBox.setValue(null);
        joinDatePicker.setValue(null);
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
