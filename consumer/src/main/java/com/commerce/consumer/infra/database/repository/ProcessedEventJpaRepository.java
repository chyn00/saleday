package com.commerce.consumer.infra.database.repository;

import com.commerce.saleday.consumer.infra.database.model.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcessedEventJpaRepository extends JpaRepository<ProcessedEvent, String> {

  @Modifying
  @Query(value = "INSERT IGNORE INTO processed_event (event_id, processed_at) VALUES (:eventId, NOW())", nativeQuery = true)
  int insertIgnore(@Param("eventId") String eventId);
}
