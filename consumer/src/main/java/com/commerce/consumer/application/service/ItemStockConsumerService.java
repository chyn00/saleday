package com.commerce.consumer.application.service;

import com.commerce.consumer.infra.database.repository.ItemStockConsumerRepositoryImpl;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.domain.stock.model.ItemStock;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemStockConsumerService {

  private final RedissonClient redissonClient;
  private final ItemStockConsumerRepositoryImpl itemStockConsumerRepository;

  /** 연산을 미리 해둠으로 인하여,
   * 병렬처리시에 멀티 인스턴스, 쓰레드에서는 계산을 빠르게 해놓고,
   * 최소한의 DB Connection을 사용한다.
   * 또한, RLock을 사용하여 동시성까지 보장한다.
   * **/
  //성능 향상을 위해 batch 로 개발
  @Transactional
  public void decreaseStock(List<DecreaseStockEvent> decreaseStockEvents)
      throws InterruptedException {

    // quantityByItemoCode
    Map<String, Long> quantityByItemCodeMap = decreaseStockEvents.stream()
        .collect(Collectors.groupingBy(
            DecreaseStockEvent::getItemCode,
            Collectors.summingLong(DecreaseStockEvent::getQuantity)
        ));

    //itemCode만 분리
    Set<String> itemCodes = quantityByItemCodeMap.keySet();


    int batchSize = 100;
    int count = 0;
    int itemStocksSize = itemCodes.size();

    // 최소화된 key만 들어온다.
    //Process & Write :itemStocks 의 quantity 변경하여, 배치처리로 저장
    for(String itemCode : itemCodes){

      String lockKey = "lock:item:stock:" + itemCode;

      RLock lock = redissonClient.getLock(lockKey);
      if (lock.tryLock(30, 3, TimeUnit.SECONDS)) {//최대 3초 락 획득, 30초 넘으면 획득 실패
        try {
          ItemStock itemStock = itemStockConsumerRepository.findItemStockByItemCode(itemCode);
          itemStock.decrease(quantityByItemCodeMap.get(itemCode));
          count++;

          // flush 사용해서 커넥션 최소화
          // 카프카 listener 에서 1차캐싱영역에 gc가 일어나지 않을정도로 제어해준 뒤, flush를 활용하여 배치 처리
          if(count % batchSize == 0 || count == itemStocksSize){
            itemStockConsumerRepository.flush();
          }
        } finally {
          lock.unlock();
        }
      } else {
        // 락 실패 시 처리(재시도 or 누락 Outbox)
      }
    }

  }

}
