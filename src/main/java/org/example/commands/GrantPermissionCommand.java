package org.example.commands;

import org.example.Matrix;
import org.example.Type;
import org.example.models.Role;
import picocli.CommandLine;

import java.util.*;

@CommandLine.Command(name="grant-permission", description = "Назначение разрешения роли и всем её потомкам")
public class GrantPermissionCommand implements Runnable {
    @CommandLine.Option(names = {"-r", "--role"}, required = true, description = "Имя роли")
    String roleName;

    @CommandLine.Option(names = {"-p", "--permissions"}, required = true, split = ",", description = "Разрешения (через запятую)")
    Set<String> permissions;

    @CommandLine.Option(names = {"-n", "--no-propagate"}, description = "Не распространять изменения на дочерние роли", defaultValue = "false")
    boolean noPropagate;

    @Override
    public void run() {
        Matrix roleMatrix = new Matrix(Type.ROLES);
        List<Role> roles = roleMatrix.readList();
        Optional<Role> targetRoleOpt = roles.stream()
                .filter(r -> r.getName().equals(roleName))
                .findFirst();

        if (targetRoleOpt.isEmpty()) {
            System.out.println("Роль не найдена: " + roleName);
            return;
        }
        Role targetRole = targetRoleOpt.get();
        targetRole.getPermissions().addAll(permissions);

        if (!noPropagate) {
            Set<String> updatedRoles = new HashSet<>();
            updatedRoles.add(targetRole.getName());

            boolean changesMade;
            do {
                changesMade = false;
                for (Role role : roles) {
                    if (role.getParents() != null && !Collections.disjoint(role.getParents(), updatedRoles)) {
                        int beforeSize = role.getPermissions().size();
                        role.getPermissions().addAll(permissions);
                        if (role.getPermissions().size() > beforeSize) {
                            updatedRoles.add(role.getName());
                            changesMade = true;
                        }
                    }
                }
            } while (changesMade);
        }

        roleMatrix.writeList(roles);
        System.out.println("Разрешения успешно добавлены:");
        System.out.println("- Роли: " + String.join(", ", noPropagate
                ? List.of(roleName)
                : findAffectedRoles(roles, roleName)));
        System.out.println("- Добавленные разрешения: " + String.join(", ", permissions));
    }

    private Set<String> findAffectedRoles(List<Role> roles, String rootRoleName) {
        Set<String> affected = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        queue.add(rootRoleName);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            affected.add(current);
            roles.stream()
                    .filter(r -> r.getParents() != null && r.getParents().contains(current))
                    .map(Role::getName)
                    .forEach(queue::add);
        }
        return affected;
    }
}