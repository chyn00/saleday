package com.commerce.saleday.order.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.commerce.saleday.discount.domain.discount.DiscountResult;
import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import com.commerce.saleday.discount.service.discount.DiscountService;
import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.service.item.ItemService;
import com.commerce.saleday.order.domain.order.model.Orders;
import com.commerce.saleday.order.domain.order.repository.OrderRepository;
import com.commerce.saleday.order.service.order.model.CreateOrderCommand;
import com.commerce.saleday.order.service.order.model.bulk.CreateBulkOrderCommand;
import com.commerce.saleday.order.service.order.model.bulk.CreateOrderItemCommand;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private ItemService itemService;

  @Mock
  private DiscountService discountService;

  @InjectMocks
  private OrderService orderService;

  @Test
  @DisplayName("단건 주문 저장 시 주문코드를 반환한다")
  void saveOrder_returnsOrderCode() {
    Item item = Item.createWithDiscountType(
        "ITEM-1",
        "item",
        "content",
        BigDecimal.valueOf(10000),
        new ArrayList<>(),
        DiscountType.NONE
    );
    when(itemService.getItem("ITEM-1")).thenReturn(item);
    when(discountService.getDiscountResult(any())).thenReturn(
        DiscountResult.builder().discountAmount(BigDecimal.ZERO).reason("NONE").build()
    );
    when(orderRepository.createOrder(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));

    String code = orderService.saveOrder(new CreateOrderCommand("ITEM-1", "USER-1", 2));

    assertThat(code).isNotBlank();
    verify(orderRepository).createOrder(any(Orders.class));
  }

  @Test
  @DisplayName("다건 주문 저장 시 동일 itemCode는 수량 합산 후 주문아이템 1건으로 저장된다")
  void saveOrders_mergesDuplicatedItemCodes() {
    Item item = Item.createWithDiscountType(
        "ITEM-1",
        "item",
        "content",
        BigDecimal.valueOf(5000),
        new ArrayList<>(),
        DiscountType.NONE
    );

    CreateBulkOrderCommand command = new CreateBulkOrderCommand(
        "USER-1",
        List.of(
            new CreateOrderItemCommand("ITEM-1", 1),
            new CreateOrderItemCommand("ITEM-1", 2)
        )
    );

    when(itemService.getItemsByItemCode(any())).thenReturn(List.of(item));
    when(discountService.getDiscountResult(any())).thenReturn(
        DiscountResult.builder().discountAmount(BigDecimal.ZERO).reason("NONE").build()
    );
    when(orderRepository.createOrder(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));

    orderService.saveOrders(command);

    ArgumentCaptor<Orders> captor = ArgumentCaptor.forClass(Orders.class);
    verify(orderRepository).createOrder(captor.capture());

    Orders saved = captor.getValue();
    assertThat(saved.getOrderItems()).hasSize(1);
    assertThat(saved.getOrderItems().get(0).getQuantity()).isEqualTo(3);
  }
}
