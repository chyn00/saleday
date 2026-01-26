package com.commerce.saleday.api.infra.transaction.outbox;

import com.commerce.saleday.common.outbox.model.OutboxMessage;
import com.commerce.saleday.common.outbox.repository.OutboxRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutboxStatusService {

  private final OutboxRepository outboxRepository;

  @Transactional
  public void markSuccess(String eventId) {
    OutboxMessage message = outboxRepository.findById(eventId)
        .orElseThrow(() -> new EntityNotFoundException("OutboxMessage not found. id=" + eventId));
    message.markSuccess();
    outboxRepository.save(message);
  }

  @Transactional
  public void markFailed(String eventId) {
    OutboxMessage message = outboxRepository.findById(eventId)
        .orElseThrow(() -> new EntityNotFoundException("OutboxMessage not found. id=" + eventId));
    message.markFailed();
    outboxRepository.save(message);
  }
}
