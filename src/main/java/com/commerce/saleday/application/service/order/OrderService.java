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

  //단건 주문 정보를 저장한다.
  @Transactional
  public String saveOrder(OrderRequestDto requestDto) {

    //아이템 객체 조회
    Item item = itemService.getItem(requestDto.getItemCode());

    //할인 객체 조회(Transactional에 묶이게 하기 위해(+객체지향 개발을 위해) this, private를 피하고 service 분리)
    DiscountResult discountResult = discountService.getDiscountResult(item.getPrice());

    //주문 정보 저장을 위해 orderItem에 세팅
    OrderItem orderItem = OrderItem.create(item, requestDto, discountResult);

    // 주문 생성 및 저장
    Orders order = Orders.create(requestDto.getUserId(), orderItem);
    return orderRepository.createOrder(order).getCode();
  }

  //todo: 다건 주문 정보 저장 필요

}
