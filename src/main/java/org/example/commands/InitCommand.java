package org.example.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@CommandLine.Command(name="init", description = "Инициализация директорий")
public class InitCommand implements Runnable {
    @Override
    public void run() {
        try {
            File dir = new File("./system");
            if (!dir.exists()) dir.mkdir();
            File rolesFile = new File(dir, "roles.json");
            File subjectFile = new File(dir, "subjects.json");
            File objectFile = new File(dir, "objects.json");

            boolean rolesCreated = rolesFile.createNewFile();
            boolean subjectsCreated = subjectFile.createNewFile();
            boolean objectsCreated = objectFile.createNewFile();

            ObjectMapper mapper = new ObjectMapper();
            if (rolesCreated) {
                mapper.writeValue(rolesFile, new HashMap<>());
            }
            if (subjectsCreated) {
                mapper.writeValue(subjectFile, new HashMap<>());
            }
            if (objectsCreated) {
                mapper.writeValue(objectFile, new HashMap<>());
            }

            System.out.println("Инициализация завершена.");

        } catch (IOException io) {
            System.err.println("Не удалось создать файлы: " + io.getMessage());
        }
    }
}
