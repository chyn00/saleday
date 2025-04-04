package com.commerce.saleday.application.service.order;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.domain.order.model.Orders;
import com.commerce.saleday.presentation.order.model.OrderRequestDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class OrderServiceTest {

  @Autowired
  private OrderService orderService;

  @Test
  void getOrder() {
    //given
    String orderCode = createOrderForTest();

    //when
    Orders orders = orderService.getOrder(orderCode);

    //then
    assertThat(orders).isNotNull();
  }

  @Test
  void getOrderItems() {
    //given
    String orderCode = createOrderForTest();

    //when
    Orders orders = orderService.getOrder(orderCode);

    //then
    assertThat(orders.getOrderItems()).isNotEmpty();
  }

  @Test
  void saveOrder() {
    //given
    OrderRequestDto orderRequestDto = OrderRequestDto
        .builder()
        .itemCode("1234")
        .userId("USER01")
        .quantity(5)
        .build();

    // when
    String orderCode = orderService.saveOrder(orderRequestDto);

    // then
    assertThat(orderCode).isNotBlank();
  }

  private String createOrderForTest() {
    //given
    OrderRequestDto orderRequestDto = OrderRequestDto
        .builder()
        .itemCode("1234")
        .userId("USER01")
        .quantity(5)
        .build();

    // 저장
    return orderService.saveOrder(orderRequestDto);
  }
}