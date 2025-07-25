package com.commerce.saleday.api.infra.database.repository.outbox;

import com.commerce.saleday.common.outbox.model.OutboxMessage;
import com.commerce.saleday.common.outbox.model.OutboxStatus;
import com.commerce.saleday.common.outbox.model.QOutboxMessage;
import com.commerce.saleday.common.outbox.repository.OutboxRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class OutboxRepositoryImpl implements OutboxRepository {

  private final OutboxJpaRepository outboxJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  // queryDsl 설정 한곳으로 통합(따로 사용시, 트랜잭션 문제)
  public OutboxRepositoryImpl(
      OutboxJpaRepository outboxJpaRepository,
      JPAQueryFactory jpaQueryFactory
  ) {
    this.outboxJpaRepository = outboxJpaRepository;
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public Optional<OutboxMessage> findById(String id) {
    return outboxJpaRepository.findById(id);
  }


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
