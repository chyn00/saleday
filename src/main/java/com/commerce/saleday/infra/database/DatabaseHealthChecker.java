package com.commerce.saleday.infra.database;


import com.commerce.saleday.infra.database.repository.DatabaseHealthCheckRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthChecker {

    private final DatabaseHealthCheckRepository databaseHealthCheckRepository ;

    public DatabaseHealthChecker(DatabaseHealthCheckRepository databaseHealthCheckRepository) {
        this.databaseHealthCheckRepository = databaseHealthCheckRepository;
    }

    @PostConstruct
    public void checkHealth() {
        try {
            databaseHealthCheckRepository.healthCheck();
            System.out.println("Database connection is alive");
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw new IllegalStateException("Database connection failed.", e);
        }
    }
}
