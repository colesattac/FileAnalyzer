package com.colesattac.fileanalyzer;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.apache.tika.Tika;
import java.io.File;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainWindowController {

    @FXML
    private TextArea metadataArea;

    @FXML
    private ImageView imageView;


    private final Tika tika = new Tika();

    @FXML
    private void handleLoadFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            displayFileMetadata(file);
        }
    }

    private void displayFileMetadata(File file) {
        metadataArea.clear();
        try {
            String metadata = tika.parseToString(file);
            String fileType = tika.detect(file);
            Image image = null;
            try {
                if (fileType.startsWith("image/")) {
                    image = new Image(new FileInputStream(file));
                }
            } catch (FileNotFoundException e){
                image = null;
            }
            if (image != null) {
                imageView.setImage(image);
            } else {
                imageView.setImage(null);
            }

            metadataArea.appendText("File Type: " + fileType + "\n");
            metadataArea.appendText("Metadata:\n" + metadata + "\n");


        } catch (Exception e) {
            metadataArea.appendText("Error processing file: " + e.getMessage());
        }
    }
}