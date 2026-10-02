package com.sofoste.apspatientdata;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public final class PatientFormApp extends Application {
    private static String screenshotPath;

    static void setScreenshotPath(String path) {
        screenshotPath = path;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(PatientFormApp.class.getResource("patient-form.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1120, 760);
        scene.getStylesheets().add(PatientFormApp.class.getResource("neptune.css").toExternalForm());

        stage.setTitle("APS Patient Data Manager · Neptune");
        stage.getIcons().add(new Image(PatientFormApp.class.getResourceAsStream("/logo.png")));
        stage.setMinWidth(980);
        stage.setMinHeight(700);
        stage.setScene(scene);

        if (screenshotPath != null) {
            loader.<PatientFormController>getController().loadDemoData();
            stage.setResizable(false);
        }
        stage.show();

        if (screenshotPath != null) {
            Platform.runLater(() -> captureScene(scene));
        }
    }

    private void captureScene(Scene scene) {
        try {
            scene.getRoot().applyCss();
            scene.getRoot().layout();
            WritableImage image = new WritableImage((int) scene.getWidth(), (int) scene.getHeight());
            scene.snapshot(image);
            File target = new File(screenshotPath).getAbsoluteFile();
            File parent = target.getParentFile();
            if (parent != null) parent.mkdirs();
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", target);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not write the interface screenshot.", exception);
        } finally {
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
