package org.example.models;

import java.util.Set;

public class Role {
    private String name;
    private Set<String> subjects;
    private Set<String> permissions;
    private Set<String> parents;

    public Role() {
    }

    public Set<String> getParents() {
        return parents;
    }

    public void setParents(Set<String> parents) {
        this.parents = parents;
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

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Role(String name, Set<String> subjects, Set<String> permissions) {
        this.name = name;
        this.subjects = subjects;
        this.permissions = permissions;
    }
}