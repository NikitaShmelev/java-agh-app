// TeacherSeed.java
package com.example.demo.seed;



import com.example.demo.entity.Teacher;
import com.example.demo.entity.TeacherCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeacherSeed {
    private final Random rnd = new Random();

    public List<Teacher> generateTeachers(int n) {
        List<Teacher> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(randomTeacher());
        }
        return list;
    }

    private Teacher randomTeacher() {
        String[] names = {"Anna", "Bartek", "Cezary", "Daria", "Ela", "Filip"};
        String[] surn = {"Nowak", "Kowalski", "Wiśniewski", "Wójcik", "Kaczmarek"};

        String fn = names[rnd.nextInt(names.length)];
        String ln = surn[rnd.nextInt(surn.length)];
        TeacherCondition c = TeacherCondition.values()[rnd.nextInt(TeacherCondition.values().length)];
        int year = 1960 + rnd.nextInt(30);
        double salary = 3000 + rnd.nextInt(3000);

        return new Teacher(fn, ln, c, year, salary);
    }
}
