package com.example.demo.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class HibernateUtil {
    private static final EntityManagerFactory EMF =
            Persistence.createEntityManagerFactory("schoolPU");

    private HibernateUtil() { }

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }
}
