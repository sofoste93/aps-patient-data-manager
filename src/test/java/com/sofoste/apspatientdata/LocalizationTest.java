package com.sofoste.apspatientdata;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalizationTest {
    private static final String BUNDLE = "com.sofoste.apspatientdata.PatientForm";
    private static final List<String> REQUIRED_KEYS = List.of(
            "firstName", "lastName", "age", "submit", "patientTab", "helpTab",
            "identityTitle", "clinicalTitle", "qrConsent", "storageBody",
            "firstNameRequired", "lastNameRequired", "ageRangeError", "ageNumberError");

    @Test
    void everySupportedLanguageContainsTheNeptuneWorkflow() {
        for (String language : List.of("en", "fr", "de", "es")) {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE, Locale.forLanguageTag(language));
            assertTrue(bundle.keySet().containsAll(REQUIRED_KEYS), "Missing translation in " + language);
        }
    }
}
