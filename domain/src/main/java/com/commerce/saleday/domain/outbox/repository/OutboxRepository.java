package com.commerce.saleday.domain.outbox.repository;

import com.commerce.saleday.domain.outbox.model.OutboxMessage;
import java.util.List;

public interface OutboxRepository {

  public OutboxMessage save(OutboxMessage outboxMessage);

  List<OutboxMessage> saveAll(List<OutboxMessage> failedMessages);

  List<OutboxMessage> findSentFailStockMessage();
}
