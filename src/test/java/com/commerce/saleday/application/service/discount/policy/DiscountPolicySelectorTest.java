package com.commerce.saleday.application.service.discount.policy;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.domain.discount.calculator.FixedDiscountCalculator;
import com.commerce.saleday.domain.discount.calculator.RateDiscountCalculator;
import com.commerce.saleday.domain.discount.model.DiscountResult;
import com.commerce.saleday.domain.discount.selector.DiscountPolicySelector;
import jakarta.transaction.Transactional;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class DiscountPolicySelectorTest {

    @Autowired
    DiscountPolicySelector discountPolicySelector;

    @Autowired
    FixedDiscountCalculator fixedDiscountCalculator;

    @Autowired
    RateDiscountCalculator rateDiscountCalculator;

    @ParameterizedTest
    @CsvSource({"10000,2000", "30000,5000", "50000,8000"})
    void fixedDiscountCalculatorTest(double price, double expected){
      // when
      DiscountResult discountResult = discountPolicySelector.select(fixedDiscountCalculator, price);

      // then
      assertThat(discountResult.getDiscountAmount()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"10000,500", "30000,3000", "50000,10000"})
    void rateDiscountCalculatorTest(double price,double expected){
      // when
      DiscountResult discountResult = discountPolicySelector.select(rateDiscountCalculator, price);

      // then
      assertThat(discountResult.getDiscountAmount()).isEqualTo(expected);
    }
}
