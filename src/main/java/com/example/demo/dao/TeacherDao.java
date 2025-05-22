package com.example.demo.dao;

import com.example.demo.entity.Teacher;
import com.example.demo.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class TeacherDao {

    /** Pobiera wszystkich nauczycieli z bazy. */
    public List<Teacher> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("FROM Teacher", Teacher.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /** Wstawia lub aktualizuje nauczyciela. */
    public void save(Teacher t) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();   // ← zamiast var
        try {
            tx.begin();
            em.merge(t);
            tx.commit();
        } catch (RuntimeException ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    /** Usuwa nauczyciela po ID (jeśli istnieje). */
    public void delete(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();   // ← zamiast var
        try {
            tx.begin();
            Teacher t = em.find(Teacher.class, id);
            if (t != null) em.remove(t);
            tx.commit();
        } finally {
            em.close();
        }
    }
}
