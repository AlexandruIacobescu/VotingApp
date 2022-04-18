package com.example.votingapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    // se retin detaliile de conectare la db in niste Stringuri statice
    public static String uname = "root", password = "Zeppelin2001", url = "jdbc:mysql://localhost:3306/corporation";
    // se retin datele de logare din DB a ultimului membru logat pe durata executiei programului
    // aceste campuri se initializeaza in momentul logarii
    public static String accountid = "", ausername = "", apassword = "";

    /**
     * Din metoda start porneste aplicatia;
     * se deschide forma initiala (Login stage)
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}