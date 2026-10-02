package com.sofoste.apspatientdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/** Saves patient forms locally using an atomic file replacement. */
public final class PatientDataStore {
    private static final String DEFAULT_LANGUAGE = "en";

    private final Map<String, String> formData;
    private final ObjectMapper objectMapper;
    private final Path dataDirectory;

    public PatientDataStore(Map<String, String> formData) {
        this(formData, defaultDirectory());
    }

    public static Path defaultDirectory() {
        return Path.of(System.getProperty("user.home"), ".aps-patient-data-manager", "data");
    }

    PatientDataStore(Map<String, String> formData, Path dataDirectory) {
        this.formData = Map.copyOf(formData);
        this.dataDirectory = dataDirectory;
        this.objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Path save(String language) throws IOException {
        String safeLanguage = normalizeLanguage(language);
        Files.createDirectories(dataDirectory);

        Path destination = dataDirectory.resolve("PatientFormData_" + safeLanguage + ".json");
        ObjectNode records = readRecords(destination);
        String recordId = Instant.now().toEpochMilli() + "-" + UUID.randomUUID();
        records.set(recordId, objectMapper.valueToTree(formData));

        Path temporary = Files.createTempFile(dataDirectory, "patient-data-", ".tmp");
        try {
            objectMapper.writeValue(temporary.toFile(), records);
            try {
                Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            Files.deleteIfExists(temporary);
            throw exception;
        }
        return destination;
    }

    private ObjectNode readRecords(Path destination) throws IOException {
        if (!Files.exists(destination)) {
            return objectMapper.createObjectNode();
        }
        JsonNode existing = objectMapper.readTree(destination.toFile());
        if (!existing.isObject()) {
            throw new IOException("The patient data file does not contain a JSON object.");
        }
        return (ObjectNode) existing;
    }

    static String normalizeLanguage(String language) {
        if (language == null || !language.toLowerCase(Locale.ROOT).matches("[a-z]{2}")) {
            return DEFAULT_LANGUAGE;
        }
        return language.toLowerCase(Locale.ROOT);
    }
}
