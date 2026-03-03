package com.commerce.consumer.application.service;

import com.commerce.consumer.infra.database.repository.ItemStockConsumerRepositoryImpl;
import com.commerce.consumer.infra.database.repository.ProcessedEventJpaRepository;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.domain.stock.model.ItemStock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemStockConsumerService {

  private final ProcessedEventJpaRepository processedEventJpaRepository;
  private final RedissonClient redissonClient;
  private final ItemStockConsumerRepositoryImpl itemStockConsumerRepository;

  /** 연산을 미리 해둠으로 인하여,
   * 병렬처리시에 멀티 인스턴스, 쓰레드에서는 계산을 빠르게 해놓고,
   * 최소한의 DB Connection을 사용한다.
   * 또한, RLock을 사용하여 동시성까지 보장한다.
   * **/
  //성능 향상을 위해 batch 로 개발
  @Transactional
  public void decreaseStock(List<DecreaseStockEvent> decreaseStockEvents) {

    List<DecreaseStockEvent> filteredIdempotentEvents = filterUnprocessedEvents(decreaseStockEvents);

    // quantityByItemoCode
    Map<String, Long> quantityByItemCodeMap = filteredIdempotentEvents.stream()
        .collect(Collectors.groupingBy(
            DecreaseStockEvent::getItemCode,
            Collectors.summingLong(DecreaseStockEvent::getQuantity)
        ));

    //itemCode만 분리
    Set<String> itemCodes = quantityByItemCodeMap.keySet();


    int batchSize = 300;
    int count = 0;
    int itemStocksSize = itemCodes.size();

    // 최소화된 key만 들어온다.
    //Process & Write :itemStocks 의 quantity 변경하여, 배치처리로 저장
    for (String itemCode : itemCodes) {

      String lockKey = "lock:item:stock:" + itemCode;

      RLock lock = redissonClient.getLock(lockKey);
      final boolean locked;
      try {
        locked = lock.tryLock(3, 30, TimeUnit.SECONDS); // 최대 3초 대기 후 락 획득 실패, 락 보유 최대 30초
      } catch (InterruptedException e) {
        // 현재 스레드가 중단 신호를 받았다는 의미이므로, 인터럽트 상태를 복구하고 상위로 전파한다.
        Thread.currentThread().interrupt();
        log.error("Interrupted while waiting lock. lockKey={}", lockKey, e);
        throw new IllegalStateException("Interrupted while waiting distributed lock", e);
      }

      if (locked) {
        try {
          ItemStock itemStock = itemStockConsumerRepository.findItemStockByItemCode(itemCode);
          itemStock.decrease(quantityByItemCodeMap.get(itemCode));
          count++;

          // flush 사용해서 커넥션 최소화
          // 카프카 listener 에서 1차캐싱영역에 gc가 일어나지 않을정도로 제어해준 뒤, flush를 활용하여 배치 처리
          if (count % batchSize == 0 || count == itemStocksSize) {
            itemStockConsumerRepository.flush();
          }
        } finally {
          lock.unlock();
        }
      } else {
        log.error("Failed to acquire distributed lock. itemCode={}", itemCode);
        throw new IllegalStateException("Failed to acquire distributed lock for itemCode: " + itemCode);
      }
    }

  }

  private List<DecreaseStockEvent> filterUnprocessedEvents(List<DecreaseStockEvent> decreaseStockEvents) {
    List<DecreaseStockEvent> result = new ArrayList<>();

    for (DecreaseStockEvent event : decreaseStockEvents) {
      if (event.getEventId() == null || event.getEventId().isBlank()) {
        throw new IllegalArgumentException("eventId is required for idempotent stock consume");
      }

      int insertedCount = processedEventJpaRepository.insertIgnore(event.getEventId());
      if (insertedCount == 1) {
        result.add(event);
      } else {
        log.debug("Skip duplicated stock event. eventId={}", event.getEventId());
      }
    }

    return result;
  }

}
