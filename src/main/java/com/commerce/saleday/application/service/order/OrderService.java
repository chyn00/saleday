package com.commerce.saleday.application.service.order;

import com.commerce.saleday.application.service.discount.DiscountService;
import com.commerce.saleday.application.service.item.ItemService;
import com.commerce.saleday.domain.discount.model.DiscountResult;
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

  private final DiscountService discountService;

  public Orders getOrder(String orderCode) {
    return orderRepository.findOrderByCode(orderCode)
        .orElseThrow(() -> new EntityNotFoundException("주문 정보가 없습니다."));
  }

  //주문 정보를 저장한다.
  @Transactional
  public String saveOrder(OrderRequestDto requestDto) {

    //아이템 객체 조회
    Item item = itemService.getItem(requestDto.getItemCode());

    //할인 객체 조회(Transactional에 묶이게 하기 위해(+객체지향 개발을 위해) this, private를 피하고 service 분리)
    DiscountResult discountResult = discountService.findDiscountResult(item.getPrice());

    //주문 정보 저장을 위해 orderItem에 세팅
    OrderItem orderItem = OrderItem
        .builder()
        .item(item)
        .quantity(requestDto.getQuantity())
        .discountPrice(discountResult.getDiscountedPrice())
        .discountPolicyContent(discountResult.getReason())
        .orderPrice(item.getPrice())
        .build();

    // 연관 관계 매핑 -> 추후 리팩토링 필요
    List<OrderItem> orderItems = new ArrayList<>();
    orderItems.add(orderItem);

    // 주문 생성 및 저장
    Orders order = Orders.create(requestDto.getUserId(), orderItems);
    return orderRepository.createOrder(order).getCode();
  }

}
