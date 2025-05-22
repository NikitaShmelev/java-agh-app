package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    protected ClassTeacher() { }                     // JPA no-arg
    public ClassTeacher(String name, int maxTeachers) {
        this.name = name;
        this.maxTeachers = maxTeachers;
    }

    /* ---------- gettery / settery ---------- */

    public Long getId()                 { return id; }
    public String getName()             { return name; }
    public void setName(String n)       { this.name = n; }
    public int getMaxTeachers()         { return maxTeachers; }
    public void setMaxTeachers(int m)   { this.maxTeachers = m; }
    public List<Teacher> getTeachers()  { return teachers; }
    public List<Rate> getRates()        { return rates; }

    /* ---------- logika pomocnicza ---------- */

    public void addTeacher(Teacher t)   { teachers.add(t); t.setGroup(this); }
    public void removeTeacher(Teacher t){ teachers.remove(t); t.setGroup(null); }

    public double getFillPercentage() {
        return maxTeachers == 0 ? 0 : 100.0 * teachers.size() / maxTeachers;
    }

    /** Krótkie info na konsoli (używa ClassContainer.summary). */
    public void summary() {
        System.out.printf("== %s ==  (nauczycieli: %d / %d) %.1f%%%n",
                name, teachers.size(), maxTeachers, getFillPercentage());
    }

    @Override
    public String toString() {
        return "ClassTeacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxTeachers=" + maxTeachers +
                '}';
    }
}
