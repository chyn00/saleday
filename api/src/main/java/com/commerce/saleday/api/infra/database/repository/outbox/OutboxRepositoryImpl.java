package com.commerce.saleday.api.infra.database.repository.outbox;

import com.commerce.saleday.domain.outbox.model.OutboxMessage;
import com.commerce.saleday.domain.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {

  private final OutboxJpaRepository outboxJpaRepository;

  @Override
  public OutboxMessage save(OutboxMessage outboxMessage) {
    return outboxJpaRepository.save(outboxMessage);
  }
}
