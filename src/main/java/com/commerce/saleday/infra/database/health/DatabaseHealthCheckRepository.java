package com.commerce.saleday.infra.database.health;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class DatabaseHealthCheckRepository{
    @PersistenceContext
    private EntityManager entityManager;

    public void checkConnection() {
        entityManager.createNativeQuery("SELECT 1").getSingleResult();
    }
}
