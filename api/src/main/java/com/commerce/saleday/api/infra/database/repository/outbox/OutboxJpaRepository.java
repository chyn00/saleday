package com.commerce.saleday.api.infra.database.repository.outbox;

import com.commerce.saleday.domain.outbox.model.OutboxMessage;
import com.commerce.saleday.domain.outbox.model.OutboxStatus;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxJpaRepository extends JpaRepository<OutboxMessage, Long> {

  //spring data jpa 장점 사용
  void deleteByStatusAndCreatedAtBefore(OutboxStatus outboxStatus, LocalDateTime oneMonthAgo);
}
