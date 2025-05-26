package com.commerce.saleday.api.infra.transaction.outbox;

import com.commerce.saleday.common.outbox.repository.OutboxRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxCleanUpScheduler {
  private final OutboxRepository outboxRepository;

  @Scheduled(cron = "0 0 3 1 * *") // 매월 1일 3시
  public void cleanUpOldMessages() {
    // 1달 이상 지난 INIT/SUCCESS 메시지 삭제 로직
    LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
    outboxRepository.deleteOldMessages(oneMonthAgo);
  }
}

