package com.commerce.saleday.order.service.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.commerce.saleday.order.domain.stock.model.ItemStock;
import com.commerce.saleday.order.domain.stock.port.ItemStockRedisPort;
import com.commerce.saleday.order.domain.stock.repository.ItemStockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemStockServiceTest {

  @Mock
  private ItemStockRepository itemStockRepository;

  @Mock
  private ItemStockRedisPort itemStockRedisPort;

  @InjectMocks
  private ItemStockService itemStockService;

  @Test
  @DisplayName("기존 Redis 재고가 있으면 add 시 누적 재고로 초기화한다")
  void addItemStock_accumulatesStock_whenRemainingExists() {
    ItemStock itemStock = org.mockito.Mockito.mock(ItemStock.class);
    ItemStock saved = org.mockito.Mockito.mock(ItemStock.class);
    when(itemStock.getQuantity()).thenReturn(10L);
    when(itemStockRepository.save(any(ItemStock.class))).thenReturn(saved);
    when(saved.getId()).thenReturn(1L);
    when(itemStockRedisPort.getItemStock("ITEM-1")).thenReturn("5");

    Long id = itemStockService.addItemStock("ITEM-1", itemStock);

    verify(itemStockRedisPort).setInitialItemStock("ITEM-1", 15L);
    verify(itemStockRepository).save(itemStock);
    assertThat(id).isEqualTo(1L);
  }

  @Test
  @DisplayName("기존 Redis 재고가 없으면 add 시 입력 재고로 초기화한다")
  void addItemStock_setsInitialStock_whenRemainingMissing() {
    ItemStock itemStock = org.mockito.Mockito.mock(ItemStock.class);
    ItemStock saved = org.mockito.Mockito.mock(ItemStock.class);
    when(itemStock.getQuantity()).thenReturn(7L);
    when(itemStockRepository.save(any(ItemStock.class))).thenReturn(saved);
    when(saved.getId()).thenReturn(2L);
    when(itemStockRedisPort.getItemStock("ITEM-2")).thenReturn(null);

    Long id = itemStockService.addItemStock("ITEM-2", itemStock);

    verify(itemStockRedisPort).setInitialItemStock("ITEM-2", 7L);
    verify(itemStockRepository).save(itemStock);
    assertThat(id).isEqualTo(2L);
  }

  @Test
  @DisplayName("decrement는 Redis port 반환값을 그대로 전달한다")
  void decrementAndCountItemStock_returnsRedisResult() {
    when(itemStockRedisPort.decrementItemStock("ITEM-3")).thenReturn(9L);

    Long result = itemStockService.decrementAndCountItemStock("ITEM-3");

    assertThat(result).isEqualTo(9L);
    verify(itemStockRedisPort).decrementItemStock("ITEM-3");
  }
}
