package com.commerce.consumer.application.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.commerce.consumer.infra.database.repository.ItemStockConsumerRepositoryImpl;
import com.commerce.consumer.infra.redis.IdempotencyChecker;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.domain.stock.model.ItemStock;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@ExtendWith(MockitoExtension.class)
class ItemStockConsumerServiceTest {

  @Mock
  private IdempotencyChecker idempotencyChecker;

  @Mock
  private RedissonClient redissonClient;

  @Mock
  private ItemStockConsumerRepositoryImpl itemStockConsumerRepository;

  @Mock
  private RLock lock;

  @Mock
  private ItemStock itemStock;

  @InjectMocks
  private ItemStockConsumerService itemStockConsumerService;

  @Test
  @DisplayName("idempotent 필터 이후 lock 획득 성공 시 재고 차감하고 flush한다")
  void decreaseStock_decreasesAndFlushes_whenLockAcquired() throws InterruptedException {
    DecreaseStockEvent event = new DecreaseStockEvent("ITEM-1", 3L, "EVENT-1");

    when(idempotencyChecker.filterIdempotentEvents(List.of(event))).thenReturn(List.of(event));
    when(redissonClient.getLock("lock:item:stock:ITEM-1")).thenReturn(lock);
    when(lock.tryLock(30, 3, java.util.concurrent.TimeUnit.SECONDS)).thenReturn(true);
    when(itemStockConsumerRepository.findItemStockByItemCode("ITEM-1")).thenReturn(itemStock);

    itemStockConsumerService.decreaseStock(List.of(event));

    verify(itemStock).decrease(3L);
    verify(itemStockConsumerRepository).flush();
    verify(lock).unlock();
  }

  @Test
  @DisplayName("lock 획득 실패 시 재고 차감/flush를 수행하지 않는다")
  void decreaseStock_skips_whenLockNotAcquired() throws InterruptedException {
    DecreaseStockEvent event = new DecreaseStockEvent("ITEM-2", 2L, "EVENT-2");

    when(idempotencyChecker.filterIdempotentEvents(List.of(event))).thenReturn(List.of(event));
    when(redissonClient.getLock("lock:item:stock:ITEM-2")).thenReturn(lock);
    when(lock.tryLock(30, 3, java.util.concurrent.TimeUnit.SECONDS)).thenReturn(false);

    itemStockConsumerService.decreaseStock(List.of(event));

    verify(itemStockConsumerRepository, never()).findItemStockByItemCode("ITEM-2");
    verify(itemStockConsumerRepository, never()).flush();
    verify(lock, never()).unlock();
  }

  @Test
  @DisplayName("idempotent 필터 결과가 비어있으면 저장소 접근을 수행하지 않는다")
  void decreaseStock_doesNothing_whenNoEventsAfterIdempotencyFilter() throws InterruptedException {
    DecreaseStockEvent event = new DecreaseStockEvent("ITEM-3", 1L, "EVENT-3");

    when(idempotencyChecker.filterIdempotentEvents(List.of(event))).thenReturn(List.of());

    itemStockConsumerService.decreaseStock(List.of(event));

    verify(redissonClient, never()).getLock(org.mockito.ArgumentMatchers.anyString());
    verify(itemStockConsumerRepository, never()).flush();
  }
}
