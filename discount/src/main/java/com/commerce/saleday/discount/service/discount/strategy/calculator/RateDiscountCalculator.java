package com.commerce.saleday.discount.service.discount.strategy.calculator;

import com.commerce.saleday.discount.domain.discount.DiscountCommand;
import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

/**
 * 조건 자체를 공통화할 필요가 생기더라도(관리자가 할인 룰을 삽입), 쿼리 중심이 아니라서 select와 calculator를 통해 만들면 유지보수가 편해짐 이렇게 개발하지
 * 않으면 쿼리 내에 기간, 할인 정책 조건 등이 붙게 되고 관리 포인트가 가려짐
 **/
@Component
public class RateDiscountCalculator implements DiscountCalculator {

  @Override
  public BigDecimal applyDiscount(DiscountCommand discountCommand) {
    return this.discountRule(discountCommand);
  }

  @Override
  public String getReason() {
    return "정률 할인";
  }

  @Override
  public DiscountType getType() {

    return DiscountType.RATED;
  }

  //할인되면 가격 0.x 가격은 버림을 비즈니스 룰로 정한다.
  private BigDecimal discountRule(DiscountCommand discountCommand) {
    BigDecimal price = discountCommand.getPrice(); // BigDecimal
    BigDecimal discountRate;

    if (price.compareTo(BigDecimal.valueOf(50000)) >= 0) {
      discountRate = BigDecimal.valueOf(0.20); // 20%
    } else if (price.compareTo(BigDecimal.valueOf(30000)) >= 0) {
      discountRate = BigDecimal.valueOf(0.10); // 10%
    } else if (price.compareTo(BigDecimal.valueOf(10000)) >= 0) {
      discountRate = BigDecimal.valueOf(0.05); // 5%
    } else {
      discountRate = BigDecimal.ZERO;
    }

    return price.multiply(discountRate).setScale(0, RoundingMode.DOWN); // 정수 원화 단위로 버림
  }
}
