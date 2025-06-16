package com.commerce.saleday.pay.infra.external;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.discount.domain.discount.DiscountResult;
import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.domain.review.model.Review;
import com.commerce.saleday.order.domain.order.model.OrderItem;
import com.commerce.saleday.order.domain.order.model.Orders;

import com.commerce.saleday.pay.TestPayApplication;
import com.commerce.saleday.pay.infra.external.kakaopay.service.KakaoPayReadyService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest(classes = TestPayApplication.class)
class KakaoPayReadyServiceTest {

  @Autowired
  KakaoPayReadyService kakaoPayReadyService;

  @Test
  void singleRequest() {
    //given
    Orders order = this.createOrderForTest();

    //when


    //then
    assertThat(kakaoPayReadyService.singlePayReadyRequest("1234",order)).isNotNull();
  }

  private Orders createOrderForTest() {

    String code = "test-1234";
    String name = "test-과자";
    String content = "test-달달한과자";
    BigDecimal price = BigDecimal.valueOf(10000);
    List<Review> reviews = new ArrayList<>();

    Item item = Item.create(1L, code, name, content, price, reviews);

    OrderItem orderItem =
        OrderItem.create(item, 3, DiscountResult.builder().discountAmount(BigDecimal.valueOf(1000)).build());

    List<OrderItem> orderItemList = new ArrayList<>();
    orderItemList.add(orderItem);

    return Orders.create("TEST-USER-01", orderItemList);
  }
}