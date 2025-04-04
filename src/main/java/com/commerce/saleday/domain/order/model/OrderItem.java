package com.commerce.saleday.domain.order.model;

import com.commerce.saleday.domain.common.BaseEntity;
import com.commerce.saleday.domain.item.model.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 하이버네이트 Proxy에서 사용하도록 단계 조정
@AllArgsConstructor(access = AccessLevel.PRIVATE)// 외부에서 사용안할거지만, Lombok기본 Builder 생성에 필요
public class OrderItem extends BaseEntity {//주문할 당시의 주문 아이템 정보를 저장하는 객체

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;// 고유번호가 아닌, +1로 생성되는 id

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  private Item item; //아이템(같은 상품인데, 할인정책이 초과되어 몇개만 할인 되는 경우 ManyToOne)

  // 연관관계의 주인
  @ManyToOne(fetch = FetchType.LAZY, optional = false)//Order없이 존재 불가
  @JoinColumn(name = "orders_id", nullable = false)
  private Orders order;

  @Column(nullable = false)
  private int quantity; // 주문 수량

  @Column(nullable = false)
  private int discountAmount;//할인 금액

  @Column(nullable = false)
  private String discountPolicyContent;//할인 정책 이유

  @Column(nullable = false)
  private double orderPrice; // (item에 있는 price) * qty - discountAmount

  @Builder
  private OrderItem(Item item, int quantity, int discountAmount,
      String discountPolicyContent, double orderPrice) {
    this.item = item;
    this.quantity = quantity;
    this.discountAmount = discountAmount;
    this.discountPolicyContent = discountPolicyContent;
    this.orderPrice = orderPrice;
  }
  
  // Order Mapping
  public void mapTo(Orders orders) {
    this.order = orders;
  }
}
