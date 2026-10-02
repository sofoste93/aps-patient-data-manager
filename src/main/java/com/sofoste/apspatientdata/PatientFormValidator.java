package com.sofoste.apspatientdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Business validation kept outside JavaFX so it stays easy to test. */
public final class PatientFormValidator {
    private PatientFormValidator() {
    }

    public static List<String> validate(Map<String, String> formData) {
        List<String> errors = new ArrayList<>();
        if (isBlank(formData.get("firstName"))) errors.add("firstNameRequired");
        if (isBlank(formData.get("lastName"))) errors.add("lastNameRequired");

        String age = formData.get("age");
        try {
            int numericAge = Integer.parseInt(age == null ? "" : age.trim());
            if (numericAge < 0 || numericAge > 130) errors.add("ageRangeError");
        } catch (NumberFormatException exception) {
            errors.add("ageNumberError");
        }
        return List.copyOf(errors);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
