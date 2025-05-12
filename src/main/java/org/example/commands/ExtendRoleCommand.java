package org.example.commands;

import org.example.Matrix;
import org.example.Type;
import org.example.models.Role;
import picocli.CommandLine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CommandLine.Command(name = "extend-role", description = "Наследование роли")
public class ExtendRoleCommand implements Runnable {
    @CommandLine.Option(names = {"-p", "--parent"}, required = true, description = "Имя родительской роли")
    String parentRoleName;

    @CommandLine.Option(names = {"-c", "--child"}, required = true, description = "Имя дочерней роли")
    String childRoleName;

    @Override
    public void run() {
        Matrix matrix = new Matrix(Type.ROLES);
        List<Role> roles = matrix.readList();

        Role parentRole = roles.stream().filter(r -> r.getName().equals(parentRoleName)).findFirst().orElse(null);
        Role childRole = roles.stream().filter(r -> r.getName().equals(childRoleName)).findFirst().orElse(null);

        if (parentRole == null || childRole == null) {
            System.out.println("Одна или обе указанные роли не найдены.");
            return;
        }

        Set<String> newPermissions = new HashSet<>(childRole.getPermissions());
        newPermissions.addAll(parentRole.getPermissions());

        Role updatedChildRole = new Role(childRoleName, childRole.getSubjects(), newPermissions);

        roles.removeIf(r -> r.getName().equals(childRoleName));
        roles.add(updatedChildRole);

        matrix.writeList(roles);
        System.out.println("Роль '" + childRoleName + "' теперь наследует разрешения от '" + parentRoleName + "'.");
    }
}