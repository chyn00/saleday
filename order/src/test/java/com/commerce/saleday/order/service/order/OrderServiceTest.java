package com.commerce.saleday.order.service.order;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.order.domain.order.model.Orders;
import com.commerce.saleday.order.service.TestOrderApplication;
import com.commerce.saleday.order.service.order.model.CreateOrderCommand;
import com.commerce.saleday.order.service.order.model.bulk.CreateBulkOrderCommand;
import com.commerce.saleday.order.service.order.model.bulk.CreateOrderItemCommand;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestOrderApplication.class)
@ActiveProfiles("test")  // application-test.yml 사용
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
    CreateOrderCommand command =
        new CreateOrderCommand("1234", "USER01", 5);

    // when
    String orderCode = orderService.saveOrder(command);

    // then
    assertThat(orderCode).isNotBlank();
  }

  //다건 주문 테스트
  @Test
  void createOrdersTest() {
    //given
    List<CreateOrderItemCommand> orderItemCommands
        = List.of(new CreateOrderItemCommand("1234",11));



    CreateBulkOrderCommand command = new CreateBulkOrderCommand("TESTER", orderItemCommands);

    // when
    String orderCode = orderService.saveOrders(command);

    // then
    assertThat(orderCode).isNotBlank();
  }

  private String createOrderForTest() {
    //given
    CreateOrderCommand command =
        new CreateOrderCommand("1234", "USER01", 5);

    // 저장
    return orderService.saveOrder(command);
  }

}