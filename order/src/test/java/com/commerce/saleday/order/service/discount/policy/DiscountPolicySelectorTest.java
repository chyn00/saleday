package com.commerce.saleday.order.service.discount.policy;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.order.domain.discount.model.DiscountType;
import com.commerce.saleday.order.service.TestOrderApplication;
import com.commerce.saleday.order.service.discount.strategy.calculator.FixedDiscountCalculator;
import com.commerce.saleday.order.service.discount.strategy.calculator.RateDiscountCalculator;
import com.commerce.saleday.order.domain.discount.DiscountResult;
import com.commerce.saleday.order.service.discount.strategy.selector.DiscountPolicySelector;
import com.commerce.saleday.order.domain.item.model.Item;
import com.commerce.saleday.order.domain.review.model.Review;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestOrderApplication.class)
@ActiveProfiles("test")  // application-test.yml 사용
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
    void fixedDiscountCalculatorTest(BigDecimal price, BigDecimal expected){
      //given
      String code = "test-1234";
      String name = "test-과자";
      String content = "test-달달한과자";
      List<Review> reviews = new ArrayList<>();

      Item item = Item.createWithDiscountType(code, name, content, price, reviews, DiscountType.FIXED);

      // when
      DiscountResult discountResult = discountPolicySelector.select(item);

      // then
      assertThat(discountResult.getDiscountAmount()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"10000,500", "30000,3000", "50000,10000"})
    void rateDiscountCalculatorTest(BigDecimal price,BigDecimal expected){
      //given
      String code = "test-1234";
      String name = "test-과자";
      String content = "test-달달한과자";
      List<Review> reviews = new ArrayList<>();

      Item item = Item.createWithDiscountType(code, name, content, price, reviews,DiscountType.RATED);

      // when
      DiscountResult discountResult = discountPolicySelector.select(item);

      // then
      assertThat(discountResult.getDiscountAmount()).isEqualTo(expected);
    }
}
