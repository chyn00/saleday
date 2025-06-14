package com.commerce.saleday.discount.service.discount.strategy.calculator;

import com.commerce.saleday.discount.domain.discount.DiscountCommand;
import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/**
 * 조건 자체를 공통화할 필요가 생기더라도(관리자가 할인 룰을 삽입), 쿼리 중심이 아니라서 select와 calculator를 통해 만들면 유지보수가 편해짐 이렇게 개발하지
 * 않으면 쿼리 내에 기간, 할인 정책 조건 등이 붙게 되고 관리 포인트가 가려짐
 **/
@Component
public class FixedDiscountCalculator implements DiscountCalculator {

  @Override
  public BigDecimal applyDiscount(DiscountCommand discountCommand) {
    return this.discountRule(discountCommand);
  }

  @Override
  public String getReason() {
    return "정액 할인";
  }

  @Override
  public DiscountType getType() {

    return DiscountType.FIXED;
  }

  //할인 룰 적용
  private BigDecimal discountRule(DiscountCommand discountCommand) {
    BigDecimal price = discountCommand.getPrice();

    //BigDecimal CompareTo는 price가 >=0 이면 compareTo 인자보다 크거나 같다는 걸 의미한다.
    if (price.compareTo(BigDecimal.valueOf(50000)) >= 0) {
      return BigDecimal.valueOf(8000);
    } else if (price.compareTo(BigDecimal.valueOf(30000)) >= 0) {
      return BigDecimal.valueOf(5000);
    } else if (price.compareTo(BigDecimal.valueOf(10000)) >= 0) {
      return BigDecimal.valueOf(2000);
    } else {
      return BigDecimal.ZERO;
    }
  }


}
