package com.example.demo.service;

import com.example.demo.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvExporter {

    public void exportTeachers(Path file) throws IOException {
        EntityManager em = HibernateUtil.getEntityManager();
        List<Object[]> rows = em.createQuery(
                        "SELECT t.firstName, t.lastName, t.condition, t.salary " +
                                "FROM Teacher t ORDER BY t.lastName", Object[].class)
                .getResultList();
        em.close();

        try (BufferedWriter w = Files.newBufferedWriter(file)) {
            w.write("firstName,lastName,condition,salary\n");
            for (Object[] r : rows) {
                w.write(String.join(",", Arrays.stream(r)
                        .map(Object::toString)
                        .toArray(String[]::new)));
                w.write("\n");
            }
        }
    }
}
