package com.commerce.saleday.order.service.discount.strategy.calculator;

import com.commerce.saleday.order.domain.discount.model.DiscountType;
import com.commerce.saleday.order.domain.item.model.Item;
import java.math.BigDecimal;

//다형성과 공통 템플릿을 활용하기 위한 인터페이스
//구현체는 fixed, rate가 있다.
public interface DiscountCalculator {

  BigDecimal applyDiscount(Item item);

  String getReason();

  DiscountType getType();
}
