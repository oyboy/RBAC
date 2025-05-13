package org.example.commands;

import org.example.Matrix;
import org.example.Type;
import org.example.models.Role;
import picocli.CommandLine;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@CommandLine.Command(name = "multi-merge", description = "Объединение нескольких ролей в новую с разными стратегиями")
public class MultiMergeCommand implements Runnable {
    @CommandLine.Option(
            names = {"-p", "--parent"},
            required = true,
            split = ",",
            description = "Исходные роли для объединения (через запятую)"
    )
    List<String> sourceRoles;

    @CommandLine.Option(
            names = {"-n", "--name"},
            required = true,
            description = "Имя новой роли"
    )
    String newRoleName;

    @CommandLine.Option(
            names = {"-m", "--merge-strategy"},
            description = "Стратегия объединения субъектов: ALL (все), INTERSECTION (пересечение), UNION (объединение)",
            defaultValue = "INTERSECTION"
    )
    String mergeStrategy;

    @CommandLine.Option(
            names = {"-ps", "--permissions-strategy"},
            description = "Стратегия для разрешений: UNION (объединение), INTERSECTION (пересечение)",
            defaultValue = "UNION"
    )
    String permissionsStrategy;

    @Override
    public void run() {
        Matrix matrix = new Matrix(Type.ROLES);
        List<Role> roles = matrix.readList();

        if (roles.stream().anyMatch(r -> r.getName().equals(newRoleName))) {
            System.out.println("Роль с именем '" + newRoleName + "' уже существует.");
            return;
        }

        List<Role> foundRoles = new ArrayList<>();
        for (String roleName : sourceRoles) {
            Role role = roles.stream()
                    .filter(r -> r.getName().equals(roleName))
                    .findFirst()
                    .orElse(null);

            if (role == null) {
                System.out.println("Роль '" + roleName + "' не найдена.");
                return;
            }
            foundRoles.add(role);
        }

        if (foundRoles.isEmpty()) {
            System.out.println("Не указано ни одной валидной роли для объединения.");
            return;
        }

        Set<String> mergedSubjects = mergeSubjects(foundRoles, mergeStrategy);
        Set<String> mergedPermissions = mergePermissions(foundRoles, permissionsStrategy);

        Role mergedRole = new Role(newRoleName, mergedSubjects, mergedPermissions);
        mergedRole.setParents(foundRoles.stream().map(Role::getName).collect(Collectors.toSet()));

        roles.add(mergedRole);
        matrix.writeList(roles);

        System.out.println("Новая роль '" + newRoleName + "' успешно создана:");
        System.out.println("Объединены роли: " + String.join(", ", sourceRoles));
        System.out.println("Стратегия субъектов: " + mergeStrategy);
        System.out.println("Стратегия разрешений: " + permissionsStrategy);
        System.out.println("Итоговые субъекты (" + mergedSubjects.size() + "): " + String.join(", ", mergedSubjects));
        System.out.println("Итоговые разрешения (" + mergedPermissions.size() + "): " + String.join(", ", mergedPermissions));
    }

    private Set<String> mergeSubjects(List<Role> roles, String strategy) {
        Set<String> result = new HashSet<>();

        if (roles.isEmpty()) return result;

        switch (strategy.toUpperCase()) {
            case "INTERSECTION":
                result = new HashSet<>(roles.get(0).getSubjects());
                for (int i = 1; i < roles.size(); i++) {
                    result.retainAll(roles.get(i).getSubjects());
                }
                break;

            case "ALL":
                Set<String> allSubjects = roles.stream()
                        .flatMap(r -> r.getSubjects().stream())
                        .collect(Collectors.toSet());
                Map<String, Long> subjectCounts = roles.stream()
                        .flatMap(r -> r.getSubjects().stream())
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                result = subjectCounts.entrySet().stream()
                        .filter(e -> e.getValue() == roles.size())
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());
                break;

            case "UNION":
            default:
                Set<String> finalResult = result;
                roles.forEach(r -> finalResult.addAll(r.getSubjects()));
                break;
        }

        return result;
    }

    private Set<String> mergePermissions(List<Role> roles, String strategy) {
        Set<String> result = new HashSet<>();
        if (roles.isEmpty()) return result;

        switch (strategy.toUpperCase()) {
            case "INTERSECTION":
                result = new HashSet<>(roles.get(0).getPermissions());
                for (int i = 1; i < roles.size(); i++) {
                    result.retainAll(roles.get(i).getPermissions());
                }
                break;

            case "UNION":
            default:
                Set<String> finalResult = result;
                roles.forEach(r -> finalResult.addAll(r.getPermissions()));
                break;
        }
        return result;
    }
}
