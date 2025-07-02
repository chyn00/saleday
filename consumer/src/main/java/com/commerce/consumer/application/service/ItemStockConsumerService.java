package com.commerce.consumer.application.service;

import com.commerce.consumer.infra.database.repository.ItemStockConsumerRepositoryImpl;
import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.domain.stock.model.ItemStock;
import java.util.List;
import java.util.Map;
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

  //dirty check update 활용하여 save 없이 저장.
  //성능 향상을 위해 batch 로 개발
  @Transactional
  public void decreaseStock(List<DecreaseStockEvent> decreaseStockEvents)
      throws InterruptedException {

    //itemCode만 분리
    List<String> itemCodes = decreaseStockEvents
        .stream()
        .map(DecreaseStockEvent::getItemCode)
        .toList();

    int batchSize = 100;
    int count = 0;
    int itemStocksSize = itemCodes.size();

    //Process & Write :itemStocks 의 quantity 변경하여, 배치처리로 저장
    for(String itemCode : itemCodes){

      String lockKey = "lock:item:stock:" + itemCode;

      RLock lock = redissonClient.getLock(lockKey);
      if (lock.tryLock(30, 3, TimeUnit.SECONDS)) {//최대 3초 락 획득, 30초 넘으면 획득 실패
        try {
          ItemStock itemStock = itemStockConsumerRepository.findItemStockByItemCode(itemCode);
          itemStock.decrease(itemStock.getQuantity());
          count++;

          //카프카 listener 에서 1차캐싱영역에 gc가 일어나지 않을정도로 제어해준 뒤, flush를 활용하여 배치 처리
          //todo: 여기서 더 최적화 할 수 있는 방법 존재(미리 계산을 해서 배치사이즈마다 1개의 쿼리만 날린다.)
          //todo: 이유는 어차피 flush도 명령어를 모아놓고 출력하는 것이기 때문에 미리 계산해서 보내도 괜찮다.
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
