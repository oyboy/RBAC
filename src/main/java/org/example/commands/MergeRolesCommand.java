package org.example.commands;

import org.example.Matrix;
import org.example.Type;
import org.example.models.Role;
import picocli.CommandLine;

import java.util.*;

@CommandLine.Command(name = "merge-roles", description = "Объединение двух ролей в новую")
public class MergeRolesCommand implements Runnable {
    @CommandLine.Option(names = {"-r1", "--role1"}, required = true, description = "Имя первой роли")
    String role1Name;

    @CommandLine.Option(names = {"-r2", "--role2"}, required = true, description = "Имя второй роли")
    String role2Name;

    @CommandLine.Option(names = {"-n", "--name"}, required = true, description = "Имя новой объединённой роли")
    String newRoleName;

    @Override
    public void run() {
        Matrix matrix = new Matrix(Type.ROLES);
        List<Role> roles = matrix.readList();

        Role role1 = roles.stream().filter(r -> r.getName().equals(role1Name)).findFirst().orElse(null);
        Role role2 = roles.stream().filter(r -> r.getName().equals(role2Name)).findFirst().orElse(null);

        if (role1 == null || role2 == null) {
            System.out.println("Одна или обе указанные роли не найдены.");
            return;
        }

        // Пересечение пользователей
        Set<String> mergedSubjects = new HashSet<>(role1.getSubjects());
        mergedSubjects.retainAll(role2.getSubjects());

        // Объединение разрешений
        Set<String> mergedPermissions = new HashSet<>(role1.getPermissions());
        mergedPermissions.addAll(role2.getPermissions());

        // Проверка на уникальность
        if (roles.stream().anyMatch(r -> r.getName().equals(newRoleName))) {
            System.out.println("Роль с таким именем уже существует.");
            return;
        }

        // Создание и добавление новой роли
        Role mergedRole = new Role(newRoleName, mergedSubjects, mergedPermissions);
        roles.add(mergedRole);
        matrix.writeList(roles);

        System.out.println("Роль '" + newRoleName + "' успешно создана на основе " + role1Name + " и " + role2Name + ".");
    }
}