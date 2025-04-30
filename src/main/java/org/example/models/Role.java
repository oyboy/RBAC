package org.example.models;

import java.util.Set;

public class Role {
    private String name;
    private Set<String> subjects;
    private Set<String> permissions;

    public Role() {
    }

    public String getName() {
        return name;
    }

    public Set<String> getSubjects() {
        return subjects;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public Role(String name, Set<String> subjects, Set<String> permissions) {
        this.name = name;
        this.subjects = subjects;
        this.permissions = permissions;
    }
}