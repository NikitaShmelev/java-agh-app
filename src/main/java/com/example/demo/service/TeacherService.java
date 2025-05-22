package com.example.demo.service;

import com.example.demo.dao.TeacherDao;
import com.example.demo.entity.Teacher;

import java.util.List;

/**
 * Warstwa biznesowa dla encji Teacher.
 */
public class TeacherService {

    private final TeacherDao dao = new TeacherDao();

    /** Zwraca wszystkich nauczycieli z bazy. */
    public List<Teacher> all() {
        return dao.findAll();
    }

    /** Zapisuje nowego lub zaktualizowanego nauczyciela. */
    public void save(Teacher t) {
        dao.save(t);
    }

    /** Usuwa nauczyciela po ID. */
    public void delete(Long id) {
        dao.delete(id);
    }
}
