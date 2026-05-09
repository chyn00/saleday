package com.commerce.consumer.infra.database.repository;

import com.commerce.saleday.consumer.infra.database.model.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcessedEventJpaRepository extends JpaRepository<ProcessedEvent, String> {

  @Modifying
  // event_id PK 충돌 시 duplicate 예외를 내지 않고 0 row 처리로 넘겨 멱등 skip 분기에 사용한다.
  @Query(value = "INSERT IGNORE INTO processed_event (event_id, processed_at) VALUES (:eventId, NOW())", nativeQuery = true)
  int insertIgnore(@Param("eventId") String eventId);
}
