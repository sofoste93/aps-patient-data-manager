module com.sofoste.apspatientdata {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires javafx.swing;

    opens com.sofoste.apspatientdata to javafx.fxml;
    exports com.sofoste.apspatientdata;
}