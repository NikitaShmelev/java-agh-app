package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * Ocena wystawiona grupie nauczycielskiej.
 */
@Entity
@Table(name = "rates")
public class Rate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 0-6 – zmieniamy nazwę kolumny, żeby uniknąć słowa kluczowego VALUE */
    @Column(name = "rating_value", nullable = false)
    private int value;

    @Column(nullable = false, length = 300)  private String comment;
    @Column(nullable = false)               private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private ClassTeacher group;

    /* ---------- gettery / settery ---------- */

    public Long getId()                 { return id; }
    public int  getValue()              { return value; }
    public void setValue(int value)     { this.value = value; }
    public String getComment()          { return comment; }
    public void setComment(String c)    { this.comment = c; }
    public LocalDate getDate()          { return date; }
    public void setDate(LocalDate d)    { this.date = d; }
    public ClassTeacher getGroup()      { return group; }
    public void setGroup(ClassTeacher g){ this.group = g; }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", value=" + value +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                '}';
    }
}
