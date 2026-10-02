package com.sofoste.apspatientdata;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class PatientFormController implements Initializable {
    private static final String BUNDLE_NAME = "com.sofoste.apspatientdata.PatientForm";

    @FXML private MenuItem englishMenu;
    @FXML private MenuItem frenchMenu;
    @FXML private MenuItem germanMenu;
    @FXML private MenuItem spanishMenu;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField age;
    @FXML private TextArea allergy;
    @FXML private TextArea medications;
    @FXML private TextArea familyHistory;
    @FXML private TextArea surgicalHistory;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label ageLabel;
    @FXML private Label allergyLabel;
    @FXML private Label medicationLabel;
    @FXML private Label familyHistoryLabel;
    @FXML private Label surgicalHistoryLabel;
    @FXML private Label identityTitleLabel;
    @FXML private Label clinicalTitleLabel;
    @FXML private Label localHintLabel;
    @FXML private Label qrTitleLabel;
    @FXML private Label qrHelpLabel;
    @FXML private Label helpTitleLabel;
    @FXML private Label helpIntroLabel;
    @FXML private Label storageTitleLabel;
    @FXML private Label storageBodyLabel;
    @FXML private Label storagePathLabel;
    @FXML private Label qrPrivacyTitleLabel;
    @FXML private Label qrPrivacyBodyLabel;
    @FXML private Label aboutBodyLabel;
    @FXML private Label statusLabel;
    @FXML private Button submitButton;
    @FXML private Button exitButton;
    @FXML private Button qrCodeButton;
    @FXML private Button openFolderButton;
    @FXML private ImageView qrCodeImage;
    @FXML private SplitMenuButton selectLanguageMenu;
    @FXML private CheckBox qrConsent;
    @FXML private Tab patientTab;
    @FXML private Tab helpTab;

    private String currentLanguage = "en";
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle ignored) {
        qrCodeButton.disableProperty().bind(qrConsent.selectedProperty().not());
        storagePathLabel.setText(PatientDataStore.defaultDirectory().toAbsolutePath().toString());
        applyLanguage(currentLanguage);
    }

    @FXML
    private void handleLanguageChangeAction(ActionEvent event) {
        applyLanguage(String.valueOf(((MenuItem) event.getSource()).getUserData()));
    }

    private void applyLanguage(String languageCode) {
        currentLanguage = PatientDataStore.normalizeLanguage(languageCode);
        resources = ResourceBundle.getBundle(BUNDLE_NAME, Locale.forLanguageTag(currentLanguage));
        selectLanguageMenu.setUserData(currentLanguage);

        firstNameLabel.setText(text("firstName"));
        lastNameLabel.setText(text("lastName"));
        ageLabel.setText(text("age"));
        allergyLabel.setText(text("allergy"));
        medicationLabel.setText(text("medications"));
        familyHistoryLabel.setText(text("familyHistory"));
        surgicalHistoryLabel.setText(text("surgicalHistory"));
        submitButton.setText(text("submit"));
        exitButton.setText(text("exit"));
        qrCodeButton.setText(text("qrCode"));
        selectLanguageMenu.setText(text("selectLanguage"));
        englishMenu.setText(text("en"));
        germanMenu.setText(text("de"));
        frenchMenu.setText(text("fr"));
        spanishMenu.setText(text("es"));
        patientTab.setText(text("patientTab"));
        helpTab.setText(text("helpTab"));
        identityTitleLabel.setText(text("identityTitle"));
        clinicalTitleLabel.setText(text("clinicalTitle"));
        localHintLabel.setText(text("localHint"));
        qrTitleLabel.setText(text("qrTitle"));
        qrHelpLabel.setText(text("qrHelp"));
        qrConsent.setText(text("qrConsent"));
        helpTitleLabel.setText(text("helpTitle"));
        helpIntroLabel.setText(text("helpIntro"));
        storageTitleLabel.setText(text("storageTitle"));
        storageBodyLabel.setText(text("storageBody"));
        qrPrivacyTitleLabel.setText(text("qrPrivacyTitle"));
        qrPrivacyBodyLabel.setText(text("qrPrivacyBody"));
        aboutBodyLabel.setText(text("aboutBody"));
        openFolderButton.setText(text("openFolder"));
        statusLabel.setText(text("readyStatus").toUpperCase(Locale.ROOT));
    }

    @FXML
    private void handleSubmitButtonAction() {
        Map<String, String> formData = collectFormData();
        List<String> errors = PatientFormValidator.validate(formData);
        if (!errors.isEmpty()) {
            String localizedErrors = errors.stream().map(this::text).reduce((a, b) -> a + "\n" + b).orElse("");
            showAlert(Alert.AlertType.WARNING, text("validationTitle"), text("validationHeader"), localizedErrors);
            statusLabel.setText(text("validationStatus").toUpperCase(Locale.ROOT));
            return;
        }

        try {
            new PatientDataStore(formData).save(currentLanguage);
            clearForm();
            statusLabel.setText(text("savedStatus").toUpperCase(Locale.ROOT));
            showAlert(Alert.AlertType.INFORMATION, text("formSubmissionTitle"),
                    text("formSubmissionHeader"), text("formSubmissionMessage"));
        } catch (IOException exception) {
            showAlert(Alert.AlertType.ERROR, text("saveErrorTitle"), text("saveErrorHeader"), exception.getMessage());
            statusLabel.setText(text("saveErrorTitle").toUpperCase(Locale.ROOT));
        }
    }

    @FXML
    private void handleQRCodeButtonAction() {
        Map<String, String> data = collectFormData();
        QRCodeGenerator.generateQRCode(buildQrContent(data), qrCodeImage);
        statusLabel.setText(text("qrReadyStatus").toUpperCase(Locale.ROOT));
    }

    @FXML
    private void handleOpenDataFolder() {
        try {
            Files.createDirectories(PatientDataStore.defaultDirectory());
            if (!Desktop.isDesktopSupported()) throw new IOException("Desktop integration is not available.");
            Desktop.getDesktop().open(PatientDataStore.defaultDirectory().toFile());
        } catch (IOException exception) {
            showAlert(Alert.AlertType.ERROR, text("openFolderError"), null,
                    PatientDataStore.defaultDirectory().toAbsolutePath().toString());
        }
    }

    private String buildQrContent(Map<String, String> data) {
        return text("firstName") + " " + data.get("firstName") + "\n"
                + text("lastName") + " " + data.get("lastName") + "\n"
                + text("age") + " " + data.get("age") + "\n"
                + text("allergy") + ": " + data.get("allergy") + "\n"
                + text("medications") + ": " + data.get("medications") + "\n"
                + text("familyHistory") + ": " + data.get("familyHistory") + "\n"
                + text("surgicalHistory") + ": " + data.get("surgicalHistory");
    }

    private Map<String, String> collectFormData() {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("firstName", firstName.getText().trim());
        data.put("lastName", lastName.getText().trim());
        data.put("age", age.getText().trim());
        data.put("allergy", allergy.getText().trim());
        data.put("medications", medications.getText().trim());
        data.put("familyHistory", familyHistory.getText().trim());
        data.put("surgicalHistory", surgicalHistory.getText().trim());
        return data;
    }

    private void clearForm() {
        firstName.clear();
        lastName.clear();
        age.clear();
        allergy.clear();
        medications.clear();
        familyHistory.clear();
        surgicalHistory.clear();
        qrCodeImage.setImage(null);
        qrConsent.setSelected(false);
    }

    private String text(String key) {
        return resources.getString(key);
    }

    private void showAlert(Alert.AlertType type, String title, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleExitButtonAction() {
        ((Stage) exitButton.getScene().getWindow()).close();
    }

    void loadDemoData() {
        firstName.setText("Amelia");
        lastName.setText("Stone");
        age.setText("36");
        allergy.setText("Penicillin");
        medications.setText("Vitamin D · 1000 IU");
        familyHistory.setText("Hypertension");
        surgicalHistory.setText("Appendectomy · 2018");
        qrConsent.setSelected(true);
        handleQRCodeButtonAction();
    }
}
