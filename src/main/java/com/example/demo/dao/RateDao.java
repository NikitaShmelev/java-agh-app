package com.example.demo.dao;

import com.example.demo.entity.ClassTeacher;
import com.example.demo.entity.Rate;
import com.example.demo.util.HibernateUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

/** Operacje CRUD + statystyki ocen. */
public class RateDao {

    /** Zapisuje ocenę; upewnia się, że grupa jest w kontekście sesji. */
    public void save(Rate r) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            /* łączymy (lub wstawiamy) grupę – teraz jest „managed” */
            ClassTeacher managedGroup = em.merge(r.getGroup());
            r.setGroup(managedGroup);

            em.persist(r);          // zapis oceny
            tx.commit();
        } finally {
            em.close();
        }
    }

    /** Zwraca statystyki:  [ groupName, count, avg ]  */
    public List<Object[]> statsPerGroup() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Rate> root = cq.from(Rate.class);

        cq.multiselect(
                root.get("group").get("name"),
                cb.count(root),
                cb.avg(root.get("value"))
        ).groupBy(root.get("group").get("name"));

        return em.createQuery(cq).getResultList();
    }
}
