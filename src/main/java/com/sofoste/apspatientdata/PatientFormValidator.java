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
        if (isBlank(formData.get("firstName"))) errors.add("First name is required.");
        if (isBlank(formData.get("lastName"))) errors.add("Last name is required.");

        String age = formData.get("age");
        try {
            int numericAge = Integer.parseInt(age == null ? "" : age.trim());
            if (numericAge < 0 || numericAge > 130) errors.add("Age must be between 0 and 130.");
        } catch (NumberFormatException exception) {
            errors.add("Age must be a whole number.");
        }
        return List.copyOf(errors);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
