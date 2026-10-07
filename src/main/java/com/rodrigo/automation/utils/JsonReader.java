package com.rodrigo.automation.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonReader {
    public static JsonNode testData;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String filePath = "src/test/resources/data/accounts.json";
            testData = mapper.readTree(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getString(String jsonPath) {
        JsonNode node = testData.at(jsonPath);
        if (node.isMissingNode() || node.isNull()) {
            throw new RuntimeException("La ruta JSON '" + jsonPath + "' no existe en el archivo.");
        }
        return node.asText();
    }
}
