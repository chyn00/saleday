package com.commerce.saleday.application.service.order;

import com.commerce.saleday.application.service.item.ItemService;
import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.order.model.OrderItem;
import com.commerce.saleday.domain.order.model.Orders;
import com.commerce.saleday.domain.order.repository.OrderRepository;
import com.commerce.saleday.presentation.order.model.OrderRequestDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  private final ItemService itemService;

  public Orders getOrder(String orderCode) {
    return orderRepository.findOrderByCode(orderCode)
        .orElseThrow(() -> new EntityNotFoundException("주문 정보가 없습니다."));
  }

  //주문 정보를 저장한다.
  @Transactional
  public String saveOrder(OrderRequestDto requestDto) {

    Item item = itemService.getItem(requestDto.getItemCode());
    OrderItem orderItem = OrderItem
        .builder()
        .item(item)
        .quantity(requestDto.getQuantity())
        .discountAmount(0)
        .discountPolicyContent("할인 정책 없음")
        .orderPrice(item.getPrice())
        .build();

    List<OrderItem> orderItems = new ArrayList<>();
    orderItems.add(orderItem);

    Orders order = Orders.create(requestDto.getUserId(), orderItems);

    return orderRepository.createOrder(order).getCode();
  }

}
