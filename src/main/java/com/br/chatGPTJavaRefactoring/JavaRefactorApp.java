package com.br.chatGPTJavaRefactoring;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.br.chatGPTJavaRefactoring.constants.Constants.INSTRUCTIONS;
import static com.br.chatGPTJavaRefactoring.service.ChatGPTClient.sendRequestToChatGpt;

public class JavaRefactorApp extends Application {

    private File selectedFile;
    private String selectedFileContent;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Java Code Refactor App");

        FileChooser fileChooser = new FileChooser();
        ComboBox<String> javaVersionComboBox = createJavaVersionComboBox();
        Button selectFileButton = createSelectFileButton(primaryStage, fileChooser);
        ProgressIndicator progressIndicator = createProgressIndicator();
        Button refactorButton = createRefactorButton(javaVersionComboBox, progressIndicator);

        VBox vbox = createMainLayout(javaVersionComboBox, selectFileButton, refactorButton, progressIndicator);

        StackPane root = new StackPane(vbox);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ComboBox<String> createJavaVersionComboBox() {
        ComboBox<String> javaVersionComboBox = new ComboBox<>();
        javaVersionComboBox.getItems().addAll("Java 17", "Java 16", "Java 15", "Java 14", "Java 13", "Java 12", "Java 11");
        return javaVersionComboBox;
    }

    private Button createSelectFileButton(Stage primaryStage, FileChooser fileChooser) {
        Button selectFileButton = new Button("Select Folder or File");
        selectFileButton.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                try {
                    List<String> lines = Files.readAllLines(selectedFile.toPath(), StandardCharsets.UTF_8);
                    selectedFileContent = String.join("\n", lines);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    selectedFileContent = null;
                }
            }
        });
        return selectFileButton;
    }

    private ProgressIndicator createProgressIndicator() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        return progressIndicator;
    }

    private Button createRefactorButton(ComboBox<String> javaVersionComboBox, ProgressIndicator progressIndicator) {
        Button refactorButton = new Button("Refactor");
        refactorButton.setOnAction(e -> {
            String languageAndVersion = javaVersionComboBox.getSelectionModel().getSelectedItem();

            refactorButton.setDisable(true);
            progressIndicator.setVisible(true);

            Task<String> refactorTask = new Task<>() {
                @Override
                protected String call() throws Exception {
                    backupFile();
                    return refactorFile(selectedFileContent, languageAndVersion);
                }
            };

            refactorTask.setOnSucceeded(event -> {
                String responseChatGpt = refactorTask.getValue();
                System.out.println(responseChatGpt);
                rewriteFile(responseChatGpt);

                refactorButton.setDisable(false);
                progressIndicator.setVisible(false);
            });

            refactorTask.setOnFailed(event -> {
                Throwable exception = refactorTask.getException();
                if (exception != null) {
                    System.out.println(exception.getMessage());
                }

                refactorButton.setDisable(false);
                progressIndicator.setVisible(false);
            });

            new Thread(refactorTask).start();
        });

        return refactorButton;
    }

    private void backupFile() {
        File backupFolder = new File(selectedFile.getParent() + "/backup");
        if (!backupFolder.exists()) {
            boolean folderCreated = backupFolder.mkdir();
            if (!folderCreated) {
                throw new RuntimeException("Failed to create backup folder.");
            }
        }
        File backupFile = new File(backupFolder, selectedFile.getName() + "BCKP");
        try {
            Files.copy(selectedFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File '" + selectedFile.getName() + "' backed up to '" + backupFolder.getPath() + "'.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create a backup of the file.");
        }
    }

    private String refactorFile(String selectedFileContent, String selectedVersion) throws Exception {
        return sendRequestToChatGpt(INSTRUCTIONS, selectedFileContent, selectedVersion);
    }

    private void rewriteFile(String newFileContent) {
        try {
            Files.write(selectedFile.toPath(), newFileContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to rewrite the file.");
        }
    }

    private VBox createMainLayout(ComboBox<String> javaVersionComboBox, Button selectFileButton, Button refactorButton,
                                  ProgressIndicator progressIndicator) {
        VBox vbox = new VBox(10, javaVersionComboBox, selectFileButton, refactorButton, progressIndicator);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        return vbox;
    }
}