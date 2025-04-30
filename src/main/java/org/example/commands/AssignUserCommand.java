package org.example.commands;

import org.example.Matrix;
import org.example.Type;
import org.example.models.Role;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name="assign-user", description = "Назначение пользователя роли")
public class AssignUserCommand implements Runnable {
    @CommandLine.Option(names = {"-r", "--role"}, required = true)
    String roleName;

    @CommandLine.Option(names = {"-u", "--user"}, required = true)
    String username;

    @Override
    public void run() {
        Matrix roleMatrix = new Matrix(Type.ROLES);
        List<Role> roles = roleMatrix.readList();

        for (Role role : roles) {
            if (role.getName().equals(roleName)) {
                if (!role.getSubjects().contains(username)) {
                    role.getSubjects().add(username);
                    roleMatrix.writeList(roles);
                    System.out.println("Пользователь добавлен к роли.");
                } else {
                    System.out.println("Пользователь уже принадлежит этой роли.");
                }
                return;
            }
        }
        System.out.println("Роль не найдена.");
    }
}