package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Encja reprezentująca pojedynczego nauczyciela.
 */
@Entity
@Table(name = "teachers")
public class Teacher implements Comparable<Teacher> {

    /* ---------- pola ---------- */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private TeacherCondition condition;

    private int    birthYear;
    private double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private ClassTeacher group;

    /* ---------- konstruktory ---------- */

    protected Teacher() { }  // wymagany przez JPA

    public Teacher(String firstName, String lastName,
                   TeacherCondition condition, int birthYear, double salary) {
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.condition  = condition;
        this.birthYear  = birthYear;
        this.salary     = salary;
    }

    /* ---------- gettery / settery ---------- */

    public Long getId()                     { return id; }
    public String getFirstName()            { return firstName; }
    public void   setFirstName(String n)    { this.firstName = n; }
    public String getLastName()             { return lastName; }
    public void   setLastName(String n)     { this.lastName = n; }
    public TeacherCondition getCondition()  { return condition; }
    public void   setCondition(TeacherCondition c) { this.condition = c; }
    public int    getBirthYear()            { return birthYear; }
    public void   setBirthYear(int y)       { this.birthYear = y; }
    public double getSalary()               { return salary; }
    public void   setSalary(double s)       { this.salary = s; }

    public ClassTeacher getGroup()                { return group; }
    public void        setGroup(ClassTeacher g)   { this.group = g; }

    /* ---------- equals / hashCode / compareTo ---------- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher that = (Teacher) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName,  that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public int compareTo(Teacher o) {
        return this.lastName.compareTo(o.lastName);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + condition + ")";
    }
}
