package com.commerce.saleday.api.service.orchestrator;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.api.controller.order.model.OrderRequestDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class OrderOrchestratorServiceTest {

  @Autowired
  OrderOrchestratorService orderOrchestratorService;

  @Test
  void orderWithLimitedStock() throws Exception {
    //given
    OrderRequestDto orderRequestDto = OrderRequestDto
        .builder()
        .itemCode("1235")
        .userId("USER01")
        .quantity(1)
        .build();

    //when
    String orderCode = orderOrchestratorService.orderWithLimitedStock(orderRequestDto);

    //then
    assertThat(orderCode).isNotBlank();

  }
}