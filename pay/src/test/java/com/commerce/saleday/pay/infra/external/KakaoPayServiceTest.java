package com.commerce.saleday.pay.infra.external;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.order.domain.discount.DiscountResult;
import com.commerce.saleday.order.domain.item.model.Item;
import com.commerce.saleday.order.domain.order.model.OrderItem;
import com.commerce.saleday.order.domain.order.model.Orders;
import com.commerce.saleday.order.domain.review.model.Review;
import com.commerce.saleday.order.service.order.OrderService;
import com.commerce.saleday.pay.domain.model.Payment;
import com.commerce.saleday.pay.domain.model.PaymentProvider;
import com.commerce.saleday.pay.domain.model.PaymentStatus;
import com.commerce.saleday.pay.infra.external.service.KakaoPayService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class KakaoPayServiceTest {

  @Autowired
  KakaoPayService kakaoPayService;

  @Autowired
  OrderService orderService;

  @Test
  void singleRequestToKakaoPay() {
    //given
    Orders order = this.createOrderForTest();

    //when
    Payment payment = Payment.create(order, PaymentStatus.PENDING, PaymentProvider.KAKAO_PAY);

    //then
    assertThat(kakaoPayService.singleRequestToKakaoPay(payment)).isNotBlank();
  }

  private Orders createOrderForTest() {

    String code = "test-1234";
    String name = "test-과자";
    String content = "test-달달한과자";
    BigDecimal price = BigDecimal.valueOf(10000);
    List<Review> reviews = new ArrayList<>();

    Item item = Item.create(code, name, content, price, reviews);

    OrderItem orderItem =
        OrderItem.create(item, 3, DiscountResult.builder().discountAmount(BigDecimal.valueOf(1000)).build());

    List<OrderItem> orderItemList = new ArrayList<>();
    orderItemList.add(orderItem);

    return Orders.create("TEST-USER-01", orderItemList);
  }
}