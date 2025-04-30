package org.example.commands;

import org.example.Matrix;
import org.example.Type;
import org.example.models.Role;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@CommandLine.Command(name="create-role", description = "Создание новой роли")
public class CreateRoleCommand implements Runnable {
    @CommandLine.Option(names = {"-n", "--name"}, required = true, description = "Название роли")
    String name;

    @Override
    public void run() {
        Matrix roleMatrix = new Matrix(Type.ROLES);
        List<Role> roles = roleMatrix.readList();

        if (roles.stream().anyMatch(r -> r.getName().equals(name))) {
            System.out.println("Роль с таким именем уже существует.");
            return;
        }

        Role role = new Role(name, new HashSet<>(), new HashSet<>());
        roles.add(role);
        roleMatrix.writeList(roles);

        System.out.println("Роль '" + name + "' успешно создана.");
    }
}