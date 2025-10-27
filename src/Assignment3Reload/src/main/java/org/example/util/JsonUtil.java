package org.example.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.model.InputData;
import org.example.model.OutputData;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static InputData readInput(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, InputData.class);
        }
    }

    public static void writeOutput(String filePath, OutputData outputData) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(outputData, writer);
        }
    }
}