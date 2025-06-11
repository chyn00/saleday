package com.commerce.saleday.discount.service.discount.config;

import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import com.commerce.saleday.discount.service.discount.strategy.calculator.DiscountCalculator;
import com.commerce.saleday.discount.service.discount.strategy.calculator.FixedDiscountCalculator;
import com.commerce.saleday.discount.service.discount.strategy.calculator.NoDiscountCalculator;
import com.commerce.saleday.discount.service.discount.strategy.calculator.RateDiscountCalculator;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscountCalculatorConfig {

  //할인 정책이 추가될 경우 여기에 추가 + 클래스 작성(복잡한 할인 확장에 좋음)
  @Bean
  public Map<DiscountType, DiscountCalculator> discountCalculatorRegistry(
      FixedDiscountCalculator fixedDiscountCalculator,
      RateDiscountCalculator rateDiscountCalculator,
      NoDiscountCalculator none
  ) {

    //생성자 주입을 this.변수에 넣는게 아니라 map으로 바로 리턴해서 사용할 수 있도록.
    //불변으로 map등록
    return Map.of(
        DiscountType.FIXED, fixedDiscountCalculator,
        DiscountType.RATED, rateDiscountCalculator,
        DiscountType.NONE, none
    );
  }
}
