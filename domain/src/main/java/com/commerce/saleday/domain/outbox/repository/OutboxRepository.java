package com.commerce.saleday.domain.outbox.repository;

import com.commerce.saleday.domain.outbox.model.OutboxMessage;

public interface OutboxRepository {

  public OutboxMessage save(OutboxMessage outboxMessage);
}
