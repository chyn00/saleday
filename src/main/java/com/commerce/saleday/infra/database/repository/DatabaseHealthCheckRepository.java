package com.commerce.saleday.infra.database.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseHealthCheckRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public int healthCheck() {
        Object result = entityManager.createNativeQuery("SELECT 1").getSingleResult();
        return Integer.parseInt(result.toString());
    }
}
