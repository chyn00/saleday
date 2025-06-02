package com.commerce.saleday.pay.infra.external.kakaopay.service;

import com.commerce.saleday.order.domain.order.model.OrderItem;
import com.commerce.saleday.order.domain.order.model.Orders;
import com.commerce.saleday.pay.domain.model.Payment;
import com.commerce.saleday.pay.infra.external.kakaopay.KakaoPayClient;
import com.commerce.saleday.pay.infra.external.kakaopay.KakaoPayProperties;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayReadyRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoPayReadyService {

  private final KakaoPayClient kakaoPayClient;
  private final KakaoPayProperties kakaoPayProperties;
  private final RedisTemplate<String, Object> redisTemplate;

  //외부 API요청
  public String singlePayReadyRequest(Payment payment) {

    KakaoPayReadyRequest kakaoPayReadyRequest = createKakaoPayReadyRequest(payment);

    // Redis에 상태 저장
    String key = kakaoPayReadyRequest.getRedisKey();
    redisTemplate.opsForValue().set(key, kakaoPayReadyRequest, Duration.ofMinutes(5));

    return kakaoPayClient.requestReady(kakaoPayReadyRequest);
  }

  //스프링 캐시 또한 proxy 때문에 public 메서드로 만들어야함
  public KakaoPayReadyRequest createKakaoPayReadyRequest(Payment payment) {
    Orders order = payment.getOrder();
    return KakaoPayReadyRequest
        .builder()
        .cid(kakaoPayProperties.getClientId())
        .partner_order_id(order.getCode())
        .partner_user_id("order")
        .item_name(summarizeItemName(order.getOrderItems()))
        .quantity("1")
        .total_amount(order.getTotalOrderPrice().toString())//orderItem의 모든 orderPrice를 계산해줘야함.
        .vat_amount(calculateVatAmount(order.getTotalOrderPrice()).toString())
        .tax_free_amount("0")
        .approval_url(kakaoPayProperties.getApprovalUrl())
        .fail_url(kakaoPayProperties.getFailUrl())
        .cancel_url(kakaoPayProperties.getCancelUrl())
        .build();
  }

  // 카카오페이에 넘길 상품 이름(보통 묶어서 ex. 아이템 외 1건으로 보냄)
  private String summarizeItemName(List<OrderItem> orderItems) {
    if (orderItems == null || orderItems.isEmpty()) {
      return "상품정보없음";
    }

    String firstItemName = orderItems.get(0).getItem().getName();
    int itemCount = orderItems.size();

    if (itemCount == 1) {
      return firstItemName;
    }

    return firstItemName + " 외 " + (itemCount - 1) + "건";
  }

  //vat 10퍼로 가정하고 계산
  private BigDecimal calculateVatAmount(BigDecimal totalOrderPrice) {
    return totalOrderPrice.multiply(new BigDecimal("0.10"))
        .setScale(0, RoundingMode.DOWN);
  }
}
