package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Grupa (klasa) nauczycieli.
 */
@Entity
@Table(name = "groups")
public class ClassTeacher {

    /* ---------- pola ---------- */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private int maxTeachers;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rate> rates = new ArrayList<>();

    /* ---------- konstruktory ---------- */

    /** wymagany przez JPA */
    protected ClassTeacher() { }

    public ClassTeacher(String name, int maxTeachers) {
        this.name = name;
        this.maxTeachers = maxTeachers;
    }

    /* ---------- get / set ---------- */

    public Long   getId()                { return id; }
    public String getName()              { return name; }
    public void   setName(String n)      { this.name = n; }

    public int    getMaxTeachers()       { return maxTeachers; }
    public void   setMaxTeachers(int m)  { this.maxTeachers = m; }

    public List<Teacher> getTeachers()   { return teachers; }
    public List<Rate>    getRates()      { return rates; }

    /* ---------- relacje pomocnicze ---------- */

    public void addTeacher(Teacher t) {
        teachers.add(t);
        t.setGroup(this);
    }
    public void removeTeacher(Teacher t) {
        teachers.remove(t);
        t.setGroup(null);
    }

    public void addRate(Rate r) {
        rates.add(r);
        r.setGroup(this);
    }
    public void removeRate(Rate r) {
        rates.remove(r);
        r.setGroup(null);
    }

    /* ---------- inne ---------- */

    /** procent zapełnienia grupy */
    public double getFillPercentage() {
        return maxTeachers == 0 ? 0.0 : 100.0 * teachers.size() / maxTeachers;
    }

    public void summary() {
        System.out.printf("== %s == (%d/%d) %.1f%%%n",
                name, teachers.size(), maxTeachers, getFillPercentage());
    }

    @Override
    public String toString() {
        return "ClassTeacher{" + "name='" + name + '\'' +
                ", max=" + maxTeachers + '}';
    }
}
