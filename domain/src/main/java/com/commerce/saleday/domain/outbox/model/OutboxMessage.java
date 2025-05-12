package com.commerce.saleday.domain.outbox.model;

import com.commerce.saleday.domain.common.BaseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//todo: 직렬화/역직렬화는 공통으로 빼 줄 필요가 있다.
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "outbox_message",
    indexes = {
        @Index(name = "idx_outbox_type_createdate_status", columnList = "type, createDate, status")
    }//인덱스 설정(아웃박스는 before commit 단계에서도 쌓이고 있기 때문에, 빠른 처리를 위해 복합 인덱스 필요)
)
public class OutboxMessage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String type;

  @Lob
  @Column(nullable = false)
  private String payload;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OutboxStatus status;

  @Column(nullable = false)
  private LocalDate createDate;

  private LocalDateTime sentAt;

  @Builder(access = AccessLevel.PRIVATE)
  private OutboxMessage(String type, Object payload, OutboxStatus status, LocalDate createDate,
      LocalDateTime sentAt) {
    this.type = type;
    this.payload = this.serializePayload(payload); // 직렬화 책임 위임
    this.status = status;
    this.createDate = createDate;
    this.sentAt = sentAt;
  }

  public static OutboxMessage of(String type, Object payload) {
    return OutboxMessage.builder()
        .type(type)
        .payload(payload)
        .status(OutboxStatus.INIT)
        .createDate(LocalDate.now())
        .build();
  }

  public void markSuccess() {
    this.status = OutboxStatus.SUCCESS;
    this.sentAt = LocalDateTime.now();
  }

  public void markFailed() {
    this.status = OutboxStatus.FAILED;
    this.sentAt = LocalDateTime.now();
  }

  // 직렬화로 저장
  private String serializePayload(Object payload) {
    try {
      return new ObjectMapper().writeValueAsString(payload);
    } catch (Exception e) {
      throw new IllegalArgumentException("Payload 직렬화 실패", e);
    }
  }

  //역직렬화 코드
  public <T> T getPayloadAs(Class<T> clazz) {
    try {
      return new ObjectMapper().readValue(this.payload, clazz);
    } catch (Exception e) {
      throw new RuntimeException("Payload 역직렬화 실패", e);
    }
  }
}

