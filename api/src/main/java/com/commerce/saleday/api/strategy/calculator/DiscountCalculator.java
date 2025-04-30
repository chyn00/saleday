package com.commerce.saleday.api.strategy.calculator;

import com.commerce.saleday.domain.item.model.Item;

//다형성과 공통 템플릿을 활용하기 위한 인터페이스
//구현체는 fixed, rate가 있다.
public interface DiscountCalculator {

  double applyDiscount(Item item);

  String getReason();
}
