package com.example.demo.container;

import com.example.demo.entity.ClassTeacher;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Kolekcja wielu grup nauczycielskich. */
public class ClassContainer {

    private final Map<String, ClassTeacher> classes = new HashMap<>();

    /* ---------- CRUD grup ---------- */

    public void addClass(String name, int maxTeachers) {
        if (classes.containsKey(name))
            throw new IllegalArgumentException("Klasa '" + name + "' już istnieje");
        classes.put(name, new ClassTeacher(name, maxTeachers));
    }

    public void removeClass(String name) {
        classes.remove(name);
    }

    public ClassTeacher get(String name) {
        return classes.get(name);
    }

    /* ---------- raporty ---------- */

    public void summary() {
        classes.values().forEach(ClassTeacher::summary);
    }

    public Map<String, Double> showFillPercentage() {
        return classes.values().stream()
                .collect(Collectors.toMap(
                        ClassTeacher::getName,
                        ClassTeacher::getFillPercentage));
    }

    public void removeEmptyClasses() {
        classes.entrySet().removeIf(e -> e.getValue().getTeachers().isEmpty());
    }

    @Override
    public String toString() {
        return "ClassContainer{" + "classes=" + classes + '}';
    }
}
