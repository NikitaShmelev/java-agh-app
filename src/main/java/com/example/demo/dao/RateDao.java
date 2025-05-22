package com.example.demo.dao;

import com.example.demo.entity.Rate;
import com.example.demo.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

/** Operacje CRUD i statystyki dla encji Rate. */
public class RateDao {

    public void save(Rate r) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();          // ← jawny typ
        try {
            tx.begin();
            em.persist(r);
            tx.commit();
        } finally {
            em.close();
        }
    }

    /** Zwraca listę Object[]{ String groupName, Long count, Double avg } */
    public List<Object[]> statsPerGroup() {
        EntityManager em = HibernateUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();        // ← jawny typ

        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Rate> root = cq.from(Rate.class);

        cq.multiselect(
                        root.get("group").get("name"),
                        cb.count(root),
                        cb.avg(root.get("value"))
                )
                .groupBy(root.get("group").get("name"));

        return em.createQuery(cq).getResultList();
    }
}
