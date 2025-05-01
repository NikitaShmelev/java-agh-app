package com.example.demo;

// ClassContainer.java
import java.util.*;
import java.util.stream.Collectors;

public class ClassContainer {
    private  Map<String, ClassTeacher> groups;

    public ClassContainer() {
        this.groups = new HashMap<>();
    }

    public void addClass(String groupName, int maxTeachers) {
        if (groups.containsKey(groupName)) {
            System.out.println("Grupa o nazwie " + groupName + " już istnieje.");
            return;
        }
        groups.put(groupName, new ClassTeacher(groupName, maxTeachers));
    }

    public void removeClass(String groupName) {
        if (groups.remove(groupName) != null) {
            System.out.println("Usunięto grupę: " + groupName);
        } else {
            System.out.println("Grupa o nazwie " + groupName + " nie istnieje.");
        }
    }

    public List<String> findEmpty() {
        return groups.entrySet().stream()
                .filter(entry -> entry.getValue().getTeachers().isEmpty())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void summary() {
        System.out.println("Podsumowanie grup nauczycielskich:");
        groups.forEach((name, group) -> {
            double fillPercentage = (double) group.getCurrentSize() / group.getMaxTeachers() * 100;
            System.out.printf("%s - zapełnienie: %.1f%%%n", name, fillPercentage);
        });
    }

    // Dodatkowe metody
    public ClassTeacher getGroup(String groupName) {
        return groups.get(groupName);
    }

    public boolean containsGroup(String groupName) {
        return groups.containsKey(groupName);
    }
}
