package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Ocena (0-6) wystawiona grupie nauczycielskiej.
 */
@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 0-6 – nazwa kolumny zmieniona, bo VALUE jest słowem kluczowym w H2 */
    @Column(name = "rating_value", nullable = false)
    private int value;

    @Column(nullable = false, length = 300)
    private String comment;

    @Column(nullable = false)
    private LocalDate date;

    /** grupa, której dotyczy ocena */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private ClassTeacher group;

    /* ---------- get / set ---------- */

    public Long getId()                   { return id; }

    public int  getValue()                { return value; }
    public void setValue(int value)       { this.value = value; }

    public String getComment()            { return comment; }
    public void   setComment(String c)    { this.comment = c; }

    public LocalDate getDate()            { return date; }
    public void      setDate(LocalDate d) { this.date = d; }

    public ClassTeacher getGroup()        { return group; }
    public void        setGroup(ClassTeacher g) { this.group = g; }

    @Override public String toString() {
        return "Rate{value=" + value + ", group=" +
                (group!=null?group.getName():"null") + '}';
    }
}
