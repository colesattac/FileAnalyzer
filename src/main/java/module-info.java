module com.colesattac.fileanalyzer.fileanalyzer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.media;
    requires javafx.web;

    requires org.apache.tika.core;

    requires org.apache.tika.parser.image;
    requires org.apache.tika.parser.font;
    requires org.apache.tika.parser.apple;
    requires org.apache.tika.parser.microsoft;



    opens com.colesattac.fileanalyzer to javafx.fxml;
    exports com.colesattac.fileanalyzer;
}