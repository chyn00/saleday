package com.commerce.saleday.consumer.infra.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "processed_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProcessedEvent {

  @Id
  @Column(name = "event_id", nullable = false, updatable = false)
  private String eventId;

  @Column(name = "processed_at", nullable = false)
  private LocalDateTime processedAt;

  private ProcessedEvent(String eventId) {
    this.eventId = eventId;
  }

  public static ProcessedEvent of(String eventId) {
    return new ProcessedEvent(eventId);
  }

  @PrePersist
  public void prePersist() {
    if (this.processedAt == null) {
      this.processedAt = LocalDateTime.now();
    }
  }
}
