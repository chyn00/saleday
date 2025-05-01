package com.commerce.saleday.api.service.discount.strategy.calculator;

import com.commerce.saleday.domain.item.model.Item;
import org.springframework.stereotype.Component;

/**
 * 조건 자체를 공통화할 필요가 생기더라도(관리자가 할인 룰을 삽입), 쿼리 중심이 아니라서 select와 calculator를 통해 만들면 유지보수가 편해짐 이렇게 개발하지
 * 않으면 쿼리 내에 기간, 할인 정책 조건 등이 붙게 되고 관리 포인트가 가려짐
 **/
@Component
public class RateDiscountCalculator implements DiscountCalculator {

  @Override
  public double applyDiscount(Item item) {
    return this.discountRule(item);
  }

  @Override
  public String getReason() {

    return "정률 할인";
  }

  //할인 룰 적용
  private double discountRule(Item item) {
    double price = item.getPrice();
    if (price >= 50000) {
      return price * 0.2;//20퍼 할인
    } else if (price >= 30000) {
      return price * 0.1;//10퍼 할인
    } else if (price >= 10000) {
      return price * 0.05;//5퍼 할인
    } else {
      return 0;
    }
  }
}
