package com.sofoste.apspatientdata;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatientFormValidatorTest {
    @Test
    void acceptsCompleteIdentityAndRealisticAge() {
        assertTrue(PatientFormValidator.validate(Map.of(
                "firstName", "Ada", "lastName", "Lovelace", "age", "36")).isEmpty());
    }

    @Test
    void rejectsMissingNamesAndInvalidAge() {
        assertEquals(3, PatientFormValidator.validate(Map.of(
                "firstName", " ", "lastName", "", "age", "unknown")).size());
    }

    @Test
    void rejectsAgeOutsideHumanRange() {
        assertEquals(1, PatientFormValidator.validate(Map.of(
                "firstName", "Ada", "lastName", "Lovelace", "age", "131")).size());
    }
}
