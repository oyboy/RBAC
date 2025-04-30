package org.example.commands;

import org.example.Matrix;
import org.example.Type;
import org.example.models.Role;
import picocli.CommandLine;

import java.util.List;
import java.util.Set;

@CommandLine.Command(name="grant-permission", description = "Назначение разрешения роли")
public class GrantPermissionCommand implements Runnable {
    @CommandLine.Option(names = {"-r", "--role"}, required = true, description = "Имя роли")
    String roleName;

    @CommandLine.Option(names = {"-p", "--permissions"}, required = true, split = ",", description = "Разрешения (через запятую)")
    Set<String> permissions;

    @Override
    public void run() {
        Matrix roleMatrix = new Matrix(Type.ROLES);
        List<Role> roles = roleMatrix.readList();

        for (Role role : roles) {
            if (role.getName().equals(roleName)) {
                role.getPermissions().addAll(permissions);
                roleMatrix.writeList(roles);
                System.out.println("Разрешения успешно добавлены роли: " + roleName);
                return;
            }
        }

        System.out.println("Роль не найдена: " + roleName);
    }
}