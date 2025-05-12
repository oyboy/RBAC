package org.example.commands;

import org.example.Matrix;
import org.example.Type;
import org.example.models.Role;
import picocli.CommandLine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CommandLine.Command(name = "switch-role", description = "Изменение роли для субъекта")
public class SwitchRoleCommand implements Runnable {
    @CommandLine.Option(names = {"-s", "--subject"}, required = true, description = "Имя субъекта")
    String subjectName;

    @CommandLine.Option(names = {"-o", "--old-role"}, required = true, description = "Текущая роль субъекта")
    String oldRoleName;

    @CommandLine.Option(names = {"-n", "--new-role"}, required = true, description = "Новая роль для субъекта")
    String newRoleName;

    @Override
    public void run() {
        Matrix matrix = new Matrix(Type.ROLES);
        List<Role> roles = matrix.readList();

        Role oldRole = roles.stream().filter(r -> r.getName().equals(oldRoleName)).findFirst().orElse(null);
        Role newRole = roles.stream().filter(r -> r.getName().equals(newRoleName)).findFirst().orElse(null);

        if (oldRole == null || newRole == null) {
            System.out.println("Одна или обе указанные роли не найдены.");
            return;
        }

        if (!oldRole.getSubjects().contains(subjectName)) {
            System.out.println("Субъект '" + subjectName + "' не имеет роли '" + oldRoleName + "'.");
            return;
        }

        Set<String> oldRoleSubjects = new HashSet<>(oldRole.getSubjects());
        oldRoleSubjects.remove(subjectName);
        Role updatedOldRole = new Role(oldRoleName, oldRoleSubjects, oldRole.getPermissions());

        Set<String> newRoleSubjects = new HashSet<>(newRole.getSubjects());
        newRoleSubjects.add(subjectName);
        Role updatedNewRole = new Role(newRoleName, newRoleSubjects, newRole.getPermissions());

        roles.removeIf(r -> r.getName().equals(oldRoleName) || r.getName().equals(newRoleName));
        roles.add(updatedOldRole);
        roles.add(updatedNewRole);

        matrix.writeList(roles);

        System.out.println("Субъект '" + subjectName + "' успешно изменен с роли '" + oldRoleName +
                "' на роль '" + newRoleName + "'.");
    }
}