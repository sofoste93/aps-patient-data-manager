package com.sofoste.apspatientdata;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PatientFormApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PatientFormApp.class.getResource("patient-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 820);
        stage.setTitle("APS Patient Form!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}