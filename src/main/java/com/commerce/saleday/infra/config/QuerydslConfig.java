package com.commerce.saleday.infra.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

  @PersistenceContext//쓰레드 세이프한 빈을 주입받아 사용
  private EntityManager entityManager;

  @Bean
  public JPAQueryFactory queryFactory() {
    return new JPAQueryFactory(entityManager);
  }
}