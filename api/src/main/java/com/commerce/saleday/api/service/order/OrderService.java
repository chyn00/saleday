package com.commerce.saleday.api.service.order;

import com.commerce.saleday.api.service.discount.DiscountService;
import com.commerce.saleday.api.service.item.ItemService;
import com.commerce.saleday.domain.discount.model.DiscountResult;
import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.order.model.OrderItem;
import com.commerce.saleday.domain.order.model.Orders;
import com.commerce.saleday.domain.order.repository.OrderRepository;
import com.commerce.saleday.api.presentation.order.model.OrderRequestDto;
import com.commerce.saleday.api.presentation.order.model.bulk.BulkOrderRequestDto;
import com.commerce.saleday.api.presentation.order.model.bulk.OrderItemRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    //할인 객체 조회
    DiscountResult discountResult = discountService.getDiscountResult(item);

    //주문 정보 저장을 위해 orderItem에 세팅
    OrderItem orderItem = OrderItem.create(item, requestDto.getQuantity(), discountResult);

    // 주문 생성 및 저장
    //List.of를 활용해 항상 order에는 orderItem이 List로 설계된 일관성 유지
    Orders order = Orders.create(requestDto.getUserId(), List.of(orderItem));
    return orderRepository.createOrder(order).getCode();
  }

  //다건 주문 정보를 저장한다.
  @Transactional
  public String saveOrders(BulkOrderRequestDto bulkOrderRequestDto) {
    //아이템 객체 조회
    List<Item> itemList = itemService.getItemsByItemCode(
        bulkOrderRequestDto
            .getOrderItemRequestList()
            .stream()
            .map(OrderItemRequest::getItemCode)
            .toList());

    // 수량 관련된 로직, 중복합산, 0초과인 경우만 필터링도 함께 포함(트랜잭션 관련없어서 private 분리)
    Map<String, Integer> quantityMapByItemCode = this.getCalculatedQuantity(
        bulkOrderRequestDto.getOrderItemRequestList());

    //주문 아이템은 주문당 리스트로 저장(추후 주문 여러개로 확장 가능성 있음)
    List<OrderItem> orderItemList = this.getCalculatedOrderItemList(quantityMapByItemCode,
        itemList);

    // 주문 생성 및 저장
    Orders order = Orders.create(bulkOrderRequestDto.getUserId(), orderItemList);

    return orderRepository.createOrder(order).getCode();
  }

  // 아이템별 수량 계산
  private Map<String, Integer> getCalculatedQuantity(
      @Valid @NotEmpty List<OrderItemRequest> orderItemRequestList) {
    return orderItemRequestList.stream()
        .filter(item -> item.getQuantity() > 0) // 0 초과만 필터링
        .collect(Collectors.toMap(
            OrderItemRequest::getItemCode,     // 키: itemCode
            OrderItemRequest::getQuantity,     // 값: quantity
            Integer::sum                       // 중복된 키(itemCode)는 수량 합산
        ));

  }

  //주문 아이템 목록 계산
  private List<OrderItem> getCalculatedOrderItemList(Map<String, Integer> quantityMapByItemCode,
      List<Item> itemList) {
    List<OrderItem> orderItemList = new ArrayList<>();

    //할인 객체 조회
    for (Item item : itemList) {
      DiscountResult discountResult = discountService.getDiscountResult(item);

      // 계산된 수량 가져오기
      int quantityByItemCode = quantityMapByItemCode.get(item.getCode());

      //주문 정보 저장을 위해 orderItem에 세팅
      OrderItem orderItem = OrderItem.create(item, quantityByItemCode, discountResult);
      orderItemList.add(orderItem);
    }

    return orderItemList;
  }


}
