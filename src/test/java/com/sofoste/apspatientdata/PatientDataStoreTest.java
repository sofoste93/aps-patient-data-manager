package com.sofoste.apspatientdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatientDataStoreTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void savesEachSubmissionWithoutOverwritingThePreviousOne() throws Exception {
        PatientDataStore store = new PatientDataStore(Map.of(
                "firstName", "Ada", "lastName", "Lovelace", "age", "36"), temporaryDirectory);

        Path destination = store.save("fr");
        store.save("fr");

        JsonNode records = new ObjectMapper().readTree(destination.toFile());
        assertEquals("PatientFormData_fr.json", destination.getFileName().toString());
        assertEquals(2, records.size());
    }

    @Test
    void unsafeLanguageFallsBackToEnglish() throws Exception {
        PatientDataStore store = new PatientDataStore(Map.of("firstName", "Ada"), temporaryDirectory);
        assertTrue(store.save("../../secret").endsWith("PatientFormData_en.json"));
    }
}
