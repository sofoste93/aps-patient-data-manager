package com.sofoste.apspatientdata;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    @FXML private Button submitButton;
    @FXML private Button exitButton;
    @FXML private Button qrCodeButton;
    @FXML private ImageView qrCodeImage;
    @FXML private SplitMenuButton selectLanguageMenu;

    private String currentLanguage = "en";
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle ignored) {
        applyLanguage(currentLanguage);
    }

    @FXML
    private void handleLanguageChangeAction(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        applyLanguage(String.valueOf(selectedItem.getUserData()));
    }

    private void applyLanguage(String languageCode) {
        currentLanguage = PatientDataStore.normalizeLanguage(languageCode);
        resources = ResourceBundle.getBundle(BUNDLE_NAME, Locale.forLanguageTag(currentLanguage));
        selectLanguageMenu.setUserData(currentLanguage);

        firstNameLabel.setText(resources.getString("firstName"));
        lastNameLabel.setText(resources.getString("lastName"));
        ageLabel.setText(resources.getString("age"));
        allergyLabel.setText(resources.getString("allergy"));
        medicationLabel.setText(resources.getString("medications"));
        familyHistoryLabel.setText(resources.getString("familyHistory"));
        surgicalHistoryLabel.setText(resources.getString("surgicalHistory"));
        submitButton.setText(resources.getString("submit"));
        exitButton.setText(resources.getString("exit"));
        qrCodeButton.setText(resources.getString("qrCode"));
        selectLanguageMenu.setText(resources.getString("selectLanguage"));
        englishMenu.setText(resources.getString("en"));
        germanMenu.setText(resources.getString("de"));
        frenchMenu.setText(resources.getString("fr"));
        spanishMenu.setText(resources.getString("es"));
    }

    @FXML
    private void handleSubmitButtonAction() {
        Map<String, String> formData = collectFormData();
        List<String> errors = PatientFormValidator.validate(formData);
        if (!errors.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, resources.getString("validationTitle"),
                    resources.getString("validationHeader"), String.join("\n", errors));
            return;
        }

        try {
            new PatientDataStore(formData).save(currentLanguage);
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, resources.getString("formSubmissionTitle"),
                    resources.getString("formSubmissionHeader"), resources.getString("formSubmissionMessage"));
        } catch (IOException exception) {
            showAlert(Alert.AlertType.ERROR, resources.getString("saveErrorTitle"),
                    resources.getString("saveErrorHeader"), exception.getMessage());
        }
    }

    @FXML
    private void handleQRCodeButtonAction() {
        Map<String, String> data = collectFormData();
        String content = "First Name: " + data.get("firstName") + "\n"
                + "Last Name: " + data.get("lastName") + "\n"
                + "Age: " + data.get("age") + "\n"
                + "Allergies: " + data.get("allergy") + "\n"
                + "Medications: " + data.get("medications") + "\n"
                + "Family Medical History: " + data.get("familyHistory") + "\n"
                + "Surgical History: " + data.get("surgicalHistory");
        QRCodeGenerator.generateQRCode(content, qrCodeImage);
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
}
