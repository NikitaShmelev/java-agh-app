package com.example.demo;

// Teacher.java
public class Teacher implements Comparable<Teacher> {
    private String firstName;
    private String lastName;
    private TeacherCondition condition;
    private int birthYear;
    private double salary;

    public Teacher(String firstName, String lastName, TeacherCondition condition,
                   int birthYear, double salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.condition = condition;
        this.birthYear = birthYear;
        this.salary = salary;
    }

    public void printing() {
        System.out.println("Nauczyciel: " + firstName + " " + lastName);
        System.out.println("Stan: " + condition);
        System.out.println("Rok urodzenia: " + birthYear);
        System.out.println("Wynagrodzenie: " + salary + " zł");
    }

    @Override
    public int compareTo(Teacher other) {
        return this.lastName.compareTo(other.lastName);
    }

    // Gettery i Settery
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public TeacherCondition getCondition() {
        return condition;
    }

    public void setCondition(TeacherCondition condition) {
        this.condition = condition;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Teacher teacher = (Teacher) obj;
        return firstName.equals(teacher.firstName) &&
                lastName.equals(teacher.lastName);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(firstName, lastName);
//    }
}