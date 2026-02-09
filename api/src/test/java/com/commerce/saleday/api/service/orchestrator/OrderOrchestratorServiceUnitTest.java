package com.commerce.saleday.api.service.orchestrator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.commerce.saleday.api.controller.order.model.OrderRequestDto;
import com.commerce.saleday.common.exception.ExceptionCode;
import com.commerce.saleday.common.exception.SaleDayException;
import com.commerce.saleday.common.outbox.repository.OutboxRepository;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.service.order.OrderService;
import com.commerce.saleday.order.service.order.model.CreateOrderCommand;
import com.commerce.saleday.order.service.stock.ItemStockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class OrderOrchestratorServiceUnitTest {

  @Mock
  private OutboxRepository outboxRepository;

  @Mock
  private ItemStockService itemStockService;

  @Mock
  private OrderService orderService;

  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  @InjectMocks
  private OrderOrchestratorService orderOrchestratorService;

  private OrderRequestDto createRequestDto(String itemCode, String userId, int quantity) {
    return OrderRequestDto.builder()
        .itemCode(itemCode)
        .userId(userId)
        .quantity(quantity)
        .build();
  }

  @Test
  @DisplayName("재고가 충분하면 주문 성공 후 이벤트를 발행한다")
  void orderWithLimitedStock_shouldReturnOrderCode_whenStockAvailable() {
    //given
    OrderRequestDto requestDto = createRequestDto("ITEM-001", "USER01", 2);
    when(itemStockService.decrementAndCountItemStock("ITEM-001")).thenReturn(5L);
    when(orderService.saveOrder(any(CreateOrderCommand.class))).thenReturn("ORDER-123");

    //when
    String result = orderOrchestratorService.orderWithLimitedStock(requestDto);

    //then
    assertThat(result).isEqualTo("ORDER-123");
    verify(applicationEventPublisher).publishEvent(any(DecreaseStockEvent.class));
    verify(itemStockService, never()).incrementAndCountItemStock(anyString());
  }

  @Test
  @DisplayName("remaining이 0이면 마지막 재고로 주문이 성공한다 (경계값)")
  void orderWithLimitedStock_shouldSucceed_whenRemainingIsZero() {
    //given
    OrderRequestDto requestDto = createRequestDto("ITEM-001", "USER01", 1);
    when(itemStockService.decrementAndCountItemStock("ITEM-001")).thenReturn(0L);
    when(orderService.saveOrder(any(CreateOrderCommand.class))).thenReturn("ORDER-LAST");

    //when
    String result = orderOrchestratorService.orderWithLimitedStock(requestDto);

    //then
    assertThat(result).isEqualTo("ORDER-LAST");
    verify(itemStockService, never()).incrementAndCountItemStock(anyString());
  }

  @Test
  @DisplayName("remaining이 음수이면 Redis 보상(increment) 후 OUT_OF_STOCK 예외를 던진다")
  void orderWithLimitedStock_shouldCompensateAndThrow_whenOutOfStock() {
    //given
    OrderRequestDto requestDto = createRequestDto("ITEM-001", "USER01", 1);
    when(itemStockService.decrementAndCountItemStock("ITEM-001")).thenReturn(-1L);

    //when & then
    assertThatThrownBy(() -> orderOrchestratorService.orderWithLimitedStock(requestDto))
        .isInstanceOf(SaleDayException.class)
        .satisfies(ex -> assertThat(((SaleDayException) ex).getExceptionCode())
            .isEqualTo(ExceptionCode.OUT_OF_STOCK));

    verify(itemStockService).incrementAndCountItemStock("ITEM-001");
    verify(orderService, never()).saveOrder(any());
    verify(applicationEventPublisher, never()).publishEvent(any());
  }

  @Test
  @DisplayName("발행된 이벤트의 eventId가 orderCode로 설정된다")
  void orderWithLimitedStock_shouldSetEventIdToOrderCode() {
    //given
    OrderRequestDto requestDto = createRequestDto("ITEM-001", "USER01", 3);
    when(itemStockService.decrementAndCountItemStock("ITEM-001")).thenReturn(10L);
    when(orderService.saveOrder(any(CreateOrderCommand.class))).thenReturn("ORDER-ABC");

    //when
    orderOrchestratorService.orderWithLimitedStock(requestDto);

    //then
    ArgumentCaptor<DecreaseStockEvent> captor = ArgumentCaptor.forClass(DecreaseStockEvent.class);
    verify(applicationEventPublisher).publishEvent(captor.capture());

    DecreaseStockEvent captured = captor.getValue();
    assertThat(captured.getEventId()).isEqualTo("ORDER-ABC");
    assertThat(captured.getItemCode()).isEqualTo("ITEM-001");
    assertThat(captured.getQuantity()).isEqualTo(3);
  }
}
