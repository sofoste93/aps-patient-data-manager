package com.sofoste.apspatientdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Process {

    private final Map<String, String> formData;
    private final ObjectMapper objectMapper;
    private final String DEFAULT_LANGUAGE = "en";

    public Process(Map<String, String> formData) {
        this.formData = formData;
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void saveDataToJson(String language) throws IOException {
        // Load the existing data from the file, if it exists
        ObjectNode dataNode = objectMapper.createObjectNode();
        String fileName = "PatientFormData_" + (language == null ? DEFAULT_LANGUAGE : language) + ".json";
        File file = new File(fileName);

        if (file.exists()) {
            dataNode = (ObjectNode) objectMapper.readTree(file);
        }

        // Add the new form data to the existing data
        ObjectNode formDataNode = objectMapper.valueToTree(formData);
        dataNode.set(String.valueOf(System.currentTimeMillis()), formDataNode);

        // Write the updated data to the file
        objectMapper.writeValue(file, dataNode);
    }
}
