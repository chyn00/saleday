package com.commerce.saleday.infra.database.health;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DatabaseHealthCheckRepository{

    private final EntityManager entityManager;

    public void checkConnection() {
        entityManager.createNativeQuery("SELECT 1").getSingleResult();
    }
}
