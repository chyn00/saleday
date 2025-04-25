package com.commerce.saleday.api.infra.database.health;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatabaseHealthChecker {

  private final DatabaseHealthCheckRepository databaseHealthCheckRepository;

  public DatabaseHealthChecker(DatabaseHealthCheckRepository databaseHealthCheckRepository) {
    this.databaseHealthCheckRepository = databaseHealthCheckRepository;
  }

  @PostConstruct
  public void checkHealth() {
    try {
      databaseHealthCheckRepository.checkConnection();
      log.info("Database connection is alive");
    } catch (CannotGetJdbcConnectionException e) {
      log.error("Database connection failed: {}", e.getMessage());
      throw new CannotGetJdbcConnectionException("Database connection failed.");
    }
  }
}
