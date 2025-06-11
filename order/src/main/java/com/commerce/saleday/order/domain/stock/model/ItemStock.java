package com.commerce.saleday.order.domain.stock.model;

import com.commerce.saleday.common.exception.ExceptionCode;
import com.commerce.saleday.common.exception.SaleDayException;
import com.commerce.saleday.common.model.BaseEntity;
import com.commerce.saleday.item.domain.item.model.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "item_stock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 하이버네이트 Proxy에서 사용하도록 단계 조정
public class ItemStock extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;// +1로 생성되는 id(고유번호)

  @OneToOne(fetch = FetchType.LAZY)//연관관계의 주인이라 lazy 가능
  @JoinColumn(name = "item_id", unique = true)//유니크는 사실 강제로 걸리는데, 명시용으로 코딩
  private Item item;

  private long quantity; // 재고 수량(세팅 안되어 있으면, 0일수도 있음)

  // Item Mapping
  public void mapTo(Item item) {
    this.item = item;
  }

  public void decrease(long quantity) {
    if (this.quantity <= 0) throw new SaleDayException(ExceptionCode.OUT_OF_STOCK);
    this.quantity -= 1;
  }

  @Builder
  public ItemStock(Item item, long quantity) {
    this.item = item;
    this.quantity = quantity;
  }
}
