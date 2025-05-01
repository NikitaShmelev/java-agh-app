package com.example.demo;

// ClassTeacher.java
import java.util.*;
import java.util.stream.Collectors;

public class ClassTeacher {
    private String groupName;
    private List<Teacher> teachers;
    private int maxTeachers;

    public ClassTeacher(String groupName, int maxTeachers) {
        this.groupName = groupName;
        this.maxTeachers = maxTeachers;
        this.teachers = new ArrayList<>();
    }

    public void addTeacher(Teacher teacher) {
        if (teachers.contains(teacher)) {
            System.out.println("Nauczyciel " + teacher.getFirstName() + " " +
                    teacher.getLastName() + " już istnieje w grupie.");
            return;
        }

        if (teachers.size() >= maxTeachers) {
            System.out.println("Nie można dodać nauczyciela - przekroczona pojemność grupy.");
            return;
        }

        teachers.add(teacher);
    }

    public void addSalary(Teacher teacher, double amount) {
        if (teachers.contains(teacher)) {
            if (teacher.getSalary() + amount < 0) {
                System.out.println("Napewno chcemy zrobić ujemne wynagrodzenie? :)");
            }
            teacher.setSalary(teacher.getSalary() + amount);
        } else {
            System.out.println("Nauczyciel nie należy do tej grupy.");
        }
    }

    public void removeTeacher(Teacher teacher) {
        if (teachers.remove(teacher)) {
            System.out.println("Usunięto nauczyciela: " + teacher.getFirstName() + " " +
                    teacher.getLastName());
        } else {
            System.out.println("Nauczyciel nie należy do tej grupy.");
        }
    }

    public void changeCondition(Teacher teacher, TeacherCondition condition) {
        if (teachers.contains(teacher)) {
            teacher.setCondition(condition);
        } else {
            System.out.println("Nauczyciel nie należy do tej grupy.");
        }
    }

    public Teacher search(String lastName) {
        return teachers.stream()
                .filter(t -> t.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    public List<Teacher> searchPartial(String part) {
        return teachers.stream()
                .filter(t -> t.getFirstName().contains(part) || t.getLastName().contains(part))
                .collect(Collectors.toList());
    }

    public long countByCondition(TeacherCondition condition) {
        return teachers.stream()
                .filter(t -> t.getCondition() == condition)
                .count();
    }

    public void summary() {
        System.out.println("Grupa: " + groupName);
        System.out.println("Liczba nauczycieli: " + teachers.size() + "/" + maxTeachers);
        teachers.forEach(Teacher::printing);
    }

    public List<Teacher> sortByName() {
        return teachers.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Teacher> sortBySalary() {
        return teachers.stream()
                .sorted(Comparator.comparingDouble(Teacher::getSalary).reversed())
                .collect(Collectors.toList());
    }

    public Teacher max() {
        return Collections.max(teachers, Comparator.comparingDouble(Teacher::getSalary));
    }

    // Gettery
    public String getGroupName() {
        return groupName;
    }

    public List<Teacher> getTeachers() {
        return new ArrayList<>(teachers);
    }

    public int getMaxTeachers() {
        return maxTeachers;
    }

    public int getCurrentSize() {
        return teachers.size();
    }
}