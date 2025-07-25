package com.commerce.consumer.infra.redis;

import com.commerce.saleday.message.stock.DecreaseStockEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdempotencyChecker {

  private final StringRedisTemplate redisTemplate;
  private static final String IDEMPOTENCY_KEY_PREFIX = "idempotency:stock:";
  private static final Duration TTL = Duration.ofDays(3);

  public List<DecreaseStockEvent> filterIdempotentEvents(List<DecreaseStockEvent> events) {
    List<DecreaseStockEvent> result = new ArrayList<>();

    for (DecreaseStockEvent event : events) {
      String eventId = event.getEventId();
      String key = IDEMPOTENCY_KEY_PREFIX + eventId;

      Boolean isNew = redisTemplate.opsForValue().setIfAbsent(key, "processed", TTL);
      if (Boolean.TRUE.equals(isNew)) {
        result.add(event); // Redis에 없던 경우만 처리 대상
      }
    }

    return result;
  }
}
