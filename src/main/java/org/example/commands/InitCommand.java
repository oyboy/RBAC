package org.example.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@CommandLine.Command(name="init", description = "Инициализация директорий")
public class InitCommand implements Runnable {
    @Override
    public void run() {
        try {
            File dir = new File("./system");
            if (!dir.exists()) dir.mkdir();
            File rolesFile = new File(dir, "roles.json");

            boolean rolesCreated = rolesFile.createNewFile();

            ObjectMapper mapper = new ObjectMapper();
            if (rolesCreated) {
                mapper.writeValue(rolesFile, new ArrayList<>());
            }

            System.out.println("Инициализация завершена.");

        } catch (IOException io) {
            System.err.println("Не удалось создать файлы: " + io.getMessage());
        }
    }
}
