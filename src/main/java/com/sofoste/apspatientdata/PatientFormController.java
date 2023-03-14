package com.sofoste.apspatientdata;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class PatientFormController implements Initializable {

    @FXML
    public MenuItem englishMenu;
    @FXML
    public MenuItem frenchMenu;
    @FXML
    public MenuItem germanMenu;
    @FXML
    public MenuItem spanishMenu;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField age;
    @FXML
    private TextArea allergy;
    @FXML
    private TextArea medications;
    @FXML
    private TextArea familyHistory;
    @FXML
    private TextArea surgicalHistory;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label allergyLabel;
    @FXML
    private Label medicationLabel;
    @FXML
    private Label familyHistoryLabel;
    @FXML
    private Label surgicalHistoryLabel;
    @FXML
    private Button submitButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button qrCodeButton;
    @FXML
    private ImageView qrCodeImage;
    @FXML
    private SplitMenuButton selectLanguageMenu;
    private String selectedLanguage;
    private final Map<String, String> formData = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setLanguage();
    }
    @FXML
    private void handleLanguageChangeAction(ActionEvent event) {
        MenuItem selectedLanguageMenu = (MenuItem) event.getSource();
        String languageCode = (String) selectedLanguageMenu.getUserData();
        Locale locale = Locale.forLanguageTag(languageCode);
        updateLanguage(String.valueOf(locale));
    }



    private void setLanguage() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("com.sofoste.apspatientdata.PatientForm", Locale.ENGLISH);
        System.out.println(resourceBundle.keySet());
        firstNameLabel.setText(resourceBundle.getString("firstName"));
        lastNameLabel.setText(resourceBundle.getString("lastName"));
        ageLabel.setText(resourceBundle.getString("age"));
        allergyLabel.setText(resourceBundle.getString("allergy"));
        medicationLabel.setText(resourceBundle.getString("medications"));
        familyHistoryLabel.setText(resourceBundle.getString("familyHistory"));
        surgicalHistoryLabel.setText(resourceBundle.getString("surgicalHistory"));
        submitButton.setText(resourceBundle.getString("submit"));
        exitButton.setText(resourceBundle.getString("exit"));
        qrCodeButton.setText(resourceBundle.getString("qrCode"));
        selectLanguageMenu.setText(resourceBundle.getString("selectLanguage"));
        germanMenu.setText(resourceBundle.getString("de"));
        englishMenu.setText(resourceBundle.getString("en"));
        frenchMenu.setText(resourceBundle.getString("fr"));
        spanishMenu.setText(resourceBundle.getString("es"));
    }

    private void updateLanguage(String languageCode) {
        Locale locale = new Locale.Builder().setLanguage(languageCode).build();
        //ResourceBundle resources = ResourceBundle.getBundle("com.sofoste.apspatientdata.PatientForm", locale);
        ResourceBundle resources = ResourceBundle.getBundle("com.sofoste.apspatientdata.PatientForm", locale);
        System.out.println(resources.keySet());
        firstNameLabel.setText(resources.getString("firstName"));
        lastNameLabel.setText(resources.getString("lastName"));
        ageLabel.setText(resources.getString("age"));
        allergyLabel.setText(resources.getString("allergy"));
        medicationLabel.setText(resources.getString("medications"));
        familyHistoryLabel.setText(resources.getString("familyHistory"));
        surgicalHistoryLabel.setText(resources.getString("surgicalHistory"));

        exitButton.setText(resources.getString("exit"));
        qrCodeButton.setText(resources.getString("qrCode"));
        submitButton.setText(resources.getString("submit"));
        selectLanguageMenu.setText(resources.getString("selectLanguage"));
        for (MenuItem item : selectLanguageMenu.getItems()) {
            String langCode = (String) item.getUserData();
            item.setText(resources.getString(langCode));
        }
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        formData.put("firstName", firstName.getText());
        formData.put("lastName", lastName.getText());
        formData.put("age", age.getText());
        formData.put("allergy", allergy.getText());
        formData.put("medications", medications.getText());
        formData.put("familyHistory", familyHistory.getText());
        formData.put("surgicalHistory", surgicalHistory.getText());

        Process process = new Process(formData);
        try {
            String selectedLocale = (String) selectLanguageMenu.getUserData();
            process.saveDataToJson(selectedLocale);
            clearForm();

            ResourceBundle bundle = ResourceBundle.getBundle("com.sofoste.apspatientdata.PatientForm",
                    new Locale.Builder().setLanguage(selectedLocale).build());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("formSubmissionTitle"));
            alert.setHeaderText(bundle.getString("formSubmissionHeader"));
            alert.setContentText(bundle.getString("formSubmissionMessage"));
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleQRCodeButtonAction(ActionEvent event) {
        String content = "First Name: " + firstName.getText() + "\n" +
                "Last Name: " + lastName.getText() + "\n" +
                "Age: " + age.getText() + "\n" +
                "Allergies: " + allergy.getText() + "\n" +
                "Medications: " + medications.getText() + "\n" +
                "Family Medical History: " + familyHistory.getText() + "\n" +
                "Surgical History: " + surgicalHistory.getText();

        QRCodeGenerator.generateQRCode(content, qrCodeImage);
    }

    private void clearForm() {
        firstName.clear();
        lastName.clear();
        age.clear();
        allergy.clear();
        medications.clear();
        familyHistory.clear();
        surgicalHistory.clear();
    }

    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

}