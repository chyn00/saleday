package com.commerce.saleday.api.infra.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * [HikariCP Connection Pool Configuration]
 * - I/O Bound 환경에서 DB 커넥션 풀은 Tomcat 워커보다 약간 작거나 같게 설정
 * - DB가 수용 가능한 커넥션 수(max_connections)도 반드시 고려
 */
@Configuration
public class HikariPoolConfig {

    // 기본 설정 값들 (필요 시 application.yml에서 주입 가능)
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private final int cpuCores = Runtime.getRuntime().availableProcessors();

    // Tomcat maxThreads 기준 커넥션 풀 설정
    private final int maxPoolSize = 100;   // 너무 높으면 DB 과부하
    private final int minIdle = cpuCores * 2;
    private final long connectionTimeoutMs = 3000;   // 최대 대기 시간, 커넥션 풀에서 DB 커넥션을 못 받을 경우, 최대 몇 ms까지 기다릴지 (예: 3초 안에 못 받으면 예외 발생)
    private final long maxLifetimeMs = 1800000;      // 최대 생존 시간, 커넥션이 DB와 연결된 후 최대 유지 가능한 시간
    private final long idleTimeoutMs = 600000;       // 유휴 커넥션 제거, 사용되지 않고 놀고 있는 커넥션을 풀에서 제거하기까지의 대기 시간


    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        config.setConnectionTimeout(connectionTimeoutMs);
        config.setMaxLifetime(maxLifetimeMs);
        config.setIdleTimeout(idleTimeoutMs);

        config.setPoolName("HikariCP");

        return new HikariDataSource(config);
    }
}
