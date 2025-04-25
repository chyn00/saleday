package com.commerce.saleday.api.application.service.order;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.api.ApiApplication;
import com.commerce.saleday.api.application.service.order.OrderService;
import com.commerce.saleday.api.domain.order.model.Orders;
import com.commerce.saleday.api.presentation.order.model.OrderRequestDto;
import com.commerce.saleday.api.presentation.order.model.bulk.BulkOrderRequestDto;
import com.commerce.saleday.api.presentation.order.model.bulk.OrderItemRequest;
import jakarta.transaction.Transactional;
import java.util.List;
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

  //다건 주문 테스트
  @Test
  void createOrdersTest() {
    //given
    List<OrderItemRequest> orderItemRequestList
        = List.of(OrderItemRequest.builder()
        .itemCode("1234")
        .quantity(11)
        .build());

    BulkOrderRequestDto bulkOrderRequestDto = BulkOrderRequestDto
        .builder()
        .userId("TESTER")
        .orderItemRequestList(orderItemRequestList)
        .build();

    // when
    String orderCode = orderService.saveOrders(bulkOrderRequestDto);

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