package com.commerce.saleday.api.service.orchestrator;

import com.commerce.saleday.api.service.stock.ItemStockService;
import com.commerce.saleday.domain.stock.model.ItemStock;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemStockOrchestratorService {

  private final RedissonClient redissonClient;
  private final ItemStockService itemStockService;
  private static final String LOCK_PREFIX = "LOCK";

  // 분산락을 활용하여 최초 세팅 시에, 정합성을 보장해준다.
  public Long saveItemWithRLock(String itemCode, ItemStock itemStock) {
    RLock lock = redissonClient.getLock(LOCK_PREFIX + itemCode);

    try {
      boolean isGetLock = lock.tryLock(15, 1, TimeUnit.SECONDS);
      if (!isGetLock) {//락을 획득 하지 못하면 종료
        return null;
      }
      return itemStockService.setUpItemStock(itemCode, itemStock);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();//사용후에도 현재 쓰레드에 락이 걸려있다면 해제
      }
    }

  }
}