package com.commerce.saleday.discount.service.discount.strategy.calculator;

import com.commerce.saleday.discount.domain.discount.DiscountCommand;
import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import java.math.BigDecimal;

//다형성과 공통 템플릿을 활용하기 위한 인터페이스
//구현체는 fixed, rate가 있다.
public interface DiscountCalculator {

  BigDecimal applyDiscount(DiscountCommand discountCommand);

  String getReason();

  DiscountType getType();
}
