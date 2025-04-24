package org.example.models;

import java.util.List;
import java.util.Set;

public class Role {
    private String name;
    private List<String> subjects;
    private List<ObjectModel> objects;

    public String getName() {
        return name;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public List<ObjectModel> getObjects() {
        return objects;
    }

    public Role(String name, List<String> subjects, List<ObjectModel> objects) {
        this.name = name;
        this.subjects = subjects;
        this.objects = objects;
    }
}