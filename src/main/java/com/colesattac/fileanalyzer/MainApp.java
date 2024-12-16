package com.colesattac.fileanalyzer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main_window.fxml"));

            Locale locale = Locale.getDefault();
            ResourceBundle resources = ResourceBundle.getBundle("messages", locale);
            fxmlLoader.setResources(resources);


            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("File Analyzer");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}