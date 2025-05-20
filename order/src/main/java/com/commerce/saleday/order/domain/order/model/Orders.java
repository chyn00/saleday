package com.commerce.saleday.order.domain.order.model;

import com.commerce.saleday.domain.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 하이버네이트 Proxy에서 사용하도록 단계 조정
public class Orders extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private LocalDate orderDate;

  @Column(nullable = false)
  private BigDecimal totalOrderPrice;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems = new ArrayList<>();

  @Builder(access = AccessLevel.PRIVATE)
  private Orders(String code, LocalDate orderDate, BigDecimal totalOrderPrice) {
    this.code = code;
    this.orderDate = orderDate;
    this.totalOrderPrice = totalOrderPrice;
    this.orderItems = new ArrayList<>();
  }

  public static Orders create(String userId, List<OrderItem> orderItems) {

    String code = generateOrderCode(userId);
    LocalDate orderDate = LocalDate.now();

    Orders order = Orders.builder()
        .code(code)
        .orderDate(orderDate)
        .totalOrderPrice(calculateTotalPrice(orderItems))
        .build();

    for (OrderItem item : orderItems) {
      order.addOrderItem(item); // 연관관계 설정
    }

    return order;
  }

  // 객체 List 저장 및 연관관계 주인 쪽에도 매핑 데이터 세팅
  public void addOrderItem(OrderItem orderItem) {
    orderItem.mapTo(this); // 연관관계 주인 세팅(객체참조라서 .add와 순서 바뀌어도 상관없으나 명시성을 위해 앞에 배치)
    this.orderItems.add(orderItem);// 객체 List 저장
  }

  private static String generateOrderCode(String userId) {
    String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // 랜덤값 일부
    return String.format("ORDER-%s-%s", userId, random);
  }


  private static BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
    if (orderItems == null || orderItems.isEmpty()) {
      return BigDecimal.ZERO;
    }

    return orderItems.stream()
        .map(OrderItem::getOrderPrice)               // BigDecimal 타입
        .reduce(BigDecimal.ZERO, BigDecimal::add);   // 안전한 초기값 + 합산
  }
}
