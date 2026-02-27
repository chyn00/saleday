package com.commerce.saleday.discount.service.discount.strategy.selector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.commerce.saleday.discount.domain.discount.DiscountCommand;
import com.commerce.saleday.discount.domain.discount.DiscountResult;
import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import com.commerce.saleday.discount.service.discount.strategy.calculator.DiscountCalculator;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiscountPolicySelectorImplTest {

  @Mock
  private DiscountCalculator fixedCalculator;

  @Test
  @DisplayName("discount type에 맞는 calculator를 선택해 결과를 반환한다")
  void select_usesCalculatorByDiscountType() {
    DiscountPolicySelectorImpl selector = new DiscountPolicySelectorImpl(
        Map.of(DiscountType.FIXED, fixedCalculator)
    );

    DiscountCommand command = DiscountCommand.builder()
        .price(BigDecimal.valueOf(10000))
        .discountType(DiscountType.FIXED)
        .build();

    when(fixedCalculator.applyDiscount(command)).thenReturn(BigDecimal.valueOf(2000));
    when(fixedCalculator.getReason()).thenReturn("FIXED");

    DiscountResult result = selector.select(command);

    assertThat(result.getDiscountAmount()).isEqualTo(BigDecimal.valueOf(2000));
    assertThat(result.getReason()).isEqualTo("FIXED");
    verify(fixedCalculator).applyDiscount(command);
  }
}
