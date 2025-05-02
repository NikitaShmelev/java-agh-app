package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeacherSeed {
    public static List<Teacher> generateTeachers(int count) {
        String[] firstNames = {"Alice", "Bob", "Clara", "David", "Eva", "Frank", "Grace", "Henry", "Ivy", "Jack"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Lopez", "Wilson"};
        TeacherCondition[] conditions = TeacherCondition.values();

        Random random = new Random();
        List<Teacher> teachers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            TeacherCondition condition = conditions[random.nextInt(conditions.length)];
            int birthYear = 1960 + random.nextInt(30); // Year between 1960-1989
            double salary = 3000 + random.nextDouble() * 4000; // Salary between 3000 and 7000

            teachers.add(new Teacher(firstName, lastName, condition, birthYear, salary));
        }

        return teachers;
    }
}
