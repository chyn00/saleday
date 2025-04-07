package com.commerce.saleday.domain.discount.selector;

import com.commerce.saleday.domain.discount.calculator.DiscountCalculator;
import com.commerce.saleday.domain.discount.model.DiscountResult;

//selector는 템플릿 콜백 패턴을 활용해, discountCalculator의 다형성을 활용하고, discountResult를 만든다.
//로그 정책 등 다양한 전체 템플릿에 적용 포인트가 생길때 더 유용하다.
public interface DiscountPolicySelector {

  DiscountResult select(DiscountCalculator discountCalculator, double price);
}
