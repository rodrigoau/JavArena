package com.rodrigo.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    static {
        try {
            // Ruta hacia tu archivo properties
            String filePath = "src/test/resources/data/accounts.properties";
            FileInputStream inputStream = new FileInputStream(filePath);

            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();

        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el archivo config.properties: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("La llave '" + key + "' no se encontró en el archivo properties.");
        }
        return value;
    }

}
