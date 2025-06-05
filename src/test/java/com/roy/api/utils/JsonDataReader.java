package com.roy.api.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Map;

public class JsonDataReader {
    private static final String TEST_DATA_PATH = "src/test/resources/login-data/";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, String> getCredentials(String filename) {
        try {
            return mapper.readValue(
                    new File(TEST_DATA_PATH + filename),
                    new TypeReference<Map<String, String>>() {
                    });
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON file: " + filename, e);
        }
    }
}