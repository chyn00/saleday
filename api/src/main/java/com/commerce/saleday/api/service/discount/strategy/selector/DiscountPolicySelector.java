package com.commerce.saleday.api.service.discount.strategy.selector;


import com.commerce.saleday.api.service.discount.strategy.calculator.DiscountCalculator;
import com.commerce.saleday.domain.discount.model.DiscountResult;
import com.commerce.saleday.domain.item.model.Item;

//selector는 템플릿 콜백 패턴을 활용해, discountCalculator의 다형성을 활용하고, discountResult를 만든다.
//selector는 템플릿 역할을 하고, strategy 역할은 각 구현체(calculator)가 한다.
public interface DiscountPolicySelector {

  DiscountResult select(DiscountCalculator discountCalculator, Item item);
}
