package com.commerce.saleday.application.service.orchestrator;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.application.service.item.ItemService;
import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.presentation.order.model.OrderRequestDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class OrderOrchestratorServiceTest {

  @Autowired
  ItemService itemService;

  @Autowired
  OrderOrchestratorService orderOrchestratorService;

  @Test
  void orderWithLimitedStock() throws Exception {
    //given
    OrderRequestDto orderRequestDto = OrderRequestDto
        .builder()
        .itemCode("1234")
        .userId("USER01")
        .quantity(5)
        .build();

    //when
    String orderCode = orderOrchestratorService.orderWithLimitedStock(orderRequestDto);

    //then
    assertThat(orderCode).isNotBlank();

  }
}