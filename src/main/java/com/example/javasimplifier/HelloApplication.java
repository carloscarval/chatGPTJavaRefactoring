package com.example.javasimplifier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Java Code Refactor App");

        FileChooser fileChooser = new FileChooser();

        ComboBox<String> javaVersionComboBox = new ComboBox<>();
        javaVersionComboBox.getItems().addAll("Java 17", "Java 16", "Java 15", "Java 14", "Java 13", "Java 12", "Java 11");

        Button selectFileButton = new Button("Select Java File");
        Button refactorButton = new Button("Refactor");

        selectFileButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                // Aqui você pode usar 'selectedFile' para enviar o arquivo selecionado para o ChatGPT.
                // Implemente a lógica de envio e processamento aqui.
            }
        });

        refactorButton.setOnAction(e -> {
            String selectedVersion = javaVersionComboBox.getSelectionModel().getSelectedItem();
            if (selectedVersion != null) {
                // Aqui você pode enviar uma solicitação para o ChatGPT com a versão selecionada.
                // Implemente a lógica de envio e processamento aqui.
            }
        });

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(javaVersionComboBox, selectFileButton, refactorButton);

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}