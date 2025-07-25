package com.commerce.saleday.common.outbox.repository;

import com.commerce.saleday.common.outbox.model.OutboxMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OutboxRepository {

  Optional<OutboxMessage> findById(String id);

  OutboxMessage save(OutboxMessage outboxMessage);

  List<OutboxMessage> saveAll(List<OutboxMessage> failedMessages);

  List<OutboxMessage> findSentFailStockMessage();

  void deleteOldMessages(LocalDateTime oneMonthAgo);
}
