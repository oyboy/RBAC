package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.models.Role;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Matrix {
    private String MATRIX_FILE_PATH = "./system/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Matrix(Type type) {
        switch (type) {
            case SUBJECTS -> MATRIX_FILE_PATH += "subjects.json";
            case OBJECTS -> MATRIX_FILE_PATH += "objects.json";
            case ROLES -> MATRIX_FILE_PATH += "roles.json";
        }
    }
    public void writeList(List<Role> roles) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(MATRIX_FILE_PATH), roles);
        } catch (IOException e) {
            System.err.println("Ошибка при записи списка ролей: " + e.getMessage());
        }
    }

    public List<Role> readList() {
        try {
            File file = new File(MATRIX_FILE_PATH);
            if (!file.exists()) return new ArrayList<>();
            return objectMapper.readValue(file, new TypeReference<List<Role>>() {});
        } catch (IOException e) {
            System.err.println("Ошибка при чтении списка ролей: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}