package com.colesattac.fileanalyzer;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;

import java.io.File;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ResourceBundle;


public class MainWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private TextArea metadataArea;

    @FXML
    private ImageView imageView;

    @FXML
    private Pane previewPane;

    private final Tika tika = new Tika();
    private MediaPlayer mediaPlayer;

    @FXML
    private void handleLoadFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            loadFileAndDisplayMetadata(file);
        }
    }

    private void loadFileAndDisplayMetadata(File file){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                displayFileMetadata(file);
                return null;
            }
        };
        task.setOnSucceeded(e -> System.out.println("Task succeeded"));
        task.setOnFailed(e -> {
            System.out.println("Task failed");
            e.getSource().getException().printStackTrace();
        });
        new Thread(task).start();
    }


    private void displayFileMetadata(File file) {
        System.out.println("displayFileMetadata called with file: " + file.getAbsolutePath());
        try {
            Metadata metadata = new Metadata();
            String fileType = tika.detect(file);
            String content = null;
            if (fileType.equals("text/plain") || fileType.equals("text/css")
                    || fileType.equals("text/x-csrc") || fileType.equals("text/x-c++src") || fileType.equals("text/x-java-source")
                    || fileType.equals("text/x-python") || fileType.equals("application/x-sh")  || fileType.equals("application/x-bat") || fileType.equals("application/octet-stream")
                    || fileType.equals("text/x-pascal") ){
                StringBuilder stringBuilder = new StringBuilder();
                try (Reader reader = new FileReader(file)) {
                    int c;
                    while ((c = reader.read()) != -1) {
                        stringBuilder.append((char) c);
                    }
                    content = stringBuilder.toString();
                    System.out.println("content = " + content);
                }catch (IOException e){
                    content =  "Error reading file: " + e.getMessage();
                }
            }else {
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    content = tika.parseToString(inputStream, metadata);
                }
            }
            final String finalContent = content;

            System.out.println("File type: " + fileType);


            Platform.runLater(() -> {
                metadataArea.clear();
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                    mediaPlayer = null;
                }
                previewPane.getChildren().clear();

                if (fileType.startsWith("image/")) {
                    imageView.setVisible(true);
                    MediaView mediaView = null;
                    previewPane.getChildren().clear();
                    Image image = null;
                    try {
                        image = new Image(new FileInputStream(file));
                    } catch (FileNotFoundException e){
                        image = null;
                    }
                    if (image != null) {
                        imageView.setImage(image);
                    } else {
                        imageView.setImage(null);
                    }

                } else if (fileType.startsWith("audio/")) {
                    imageView.setVisible(false);
                    Media media = new Media(file.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    MediaView mediaView = new MediaView(mediaPlayer);
                    previewPane.getChildren().add(mediaView);
                    mediaPlayer.play();


                }  else if (fileType.startsWith("video/")) {
                    imageView.setVisible(false);
                    Media media = new Media(file.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    MediaView mediaView = new MediaView(mediaPlayer);
                    previewPane.getChildren().add(mediaView);
                    mediaPlayer.play();
                }else if (fileType.equals("application/pdf")){
                    imageView.setVisible(false);
                    WebView webView = new WebView();
                    webView.getEngine().load(file.toURI().toString());
                    previewPane.getChildren().add(webView);

                }else if (fileType.equals("text/plain") || fileType.equals("text/css")
                        || fileType.equals("text/x-csrc") || fileType.equals("text/x-c++src") || fileType.equals("text/x-java-source")
                        || fileType.equals("text/x-python") || fileType.equals("application/x-sh")  || fileType.equals("application/x-bat") || fileType.equals("application/octet-stream")
                        || fileType.equals("text/x-pascal") ){
                    imageView.setVisible(false);
                    WebView webView = new WebView();
                    webView.getEngine().loadContent(finalContent);
                    previewPane.getChildren().add(webView);
                } else {
                    imageView.setVisible(false);
                    MediaView mediaView = null;
                    previewPane.getChildren().clear();

                }


                metadataArea.appendText(resources.getString("fileType") + ": " + fileType + "\n");
                metadataArea.appendText(resources.getString("metadata") + ":\n");
                Iterable<String> names = Arrays.asList(metadata.names());
                for (String name : names){
                    metadataArea.appendText("\t"+name + ": " + metadata.get(name) + "\n");
                }
            });


        } catch (Exception e) {
            Platform.runLater(()-> {
                metadataArea.appendText(resources.getString("error") + ": " + e.getMessage());
                e.printStackTrace();
            });
        }
    }
}