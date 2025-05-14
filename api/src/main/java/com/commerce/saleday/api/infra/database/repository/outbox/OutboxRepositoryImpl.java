package com.commerce.saleday.api.infra.database.repository.outbox;

import com.commerce.saleday.domain.outbox.model.OutboxMessage;
import com.commerce.saleday.domain.outbox.model.OutboxStatus;
import com.commerce.saleday.domain.outbox.model.QOutboxMessage;
import com.commerce.saleday.domain.outbox.repository.OutboxRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {

  private final OutboxJpaRepository outboxJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public OutboxMessage save(OutboxMessage outboxMessage) {
    return outboxJpaRepository.save(outboxMessage);
  }

  @Override
  public List<OutboxMessage> saveAll(List<OutboxMessage> failedMessages) {
    return outboxJpaRepository.saveAll(failedMessages);
  }

  @Override
  public List<OutboxMessage> findSentFailStockMessage() {
    QOutboxMessage outbox = QOutboxMessage.outboxMessage;

    return jpaQueryFactory
        .selectFrom(outbox)
        .where(
            outbox.type.eq("stock"),
            outbox.createDate.lt(LocalDate.now()),
            outbox.status.eq(OutboxStatus.FAILED)
        )
        .fetch();
  }

  @Override
  public void deleteOldMessages(LocalDateTime oneMonthAgo) {
    outboxJpaRepository.deleteByStatusAndCreatedAtBefore(OutboxStatus.INIT, oneMonthAgo);
    outboxJpaRepository.deleteByStatusAndCreatedAtBefore(OutboxStatus.SUCCESS, oneMonthAgo);
  }
}
