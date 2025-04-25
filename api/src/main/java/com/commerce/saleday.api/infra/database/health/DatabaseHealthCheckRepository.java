package com.commerce.saleday.api.infra.database.health;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DatabaseHealthCheckRepository {

  private final EntityManager entityManager;

  public void checkConnection() {
    entityManager.createNativeQuery("SELECT 1").getSingleResult();
  }
}
