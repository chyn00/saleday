package com.commerce.saleday.infra.database.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
