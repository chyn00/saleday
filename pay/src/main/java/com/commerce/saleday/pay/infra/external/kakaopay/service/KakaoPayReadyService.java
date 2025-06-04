package com.commerce.saleday.pay.infra.external.kakaopay.service;

import com.commerce.saleday.order.domain.order.model.OrderItem;
import com.commerce.saleday.order.domain.order.model.Orders;
import com.commerce.saleday.order.service.order.OrderService;
import com.commerce.saleday.pay.infra.external.common.AbstractPayService;
import com.commerce.saleday.pay.infra.external.kakaopay.KakaoPayClient;
import com.commerce.saleday.pay.infra.external.kakaopay.KakaoPayProperties;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayForApproval;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayReadyRequest;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayReadyResponse;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoPayReadyService extends AbstractPayService {

  public static final String KAKAO_PAY_FIX_QUANTITY = "1";
  public static final String ZERO = "0";
  private final KakaoPayClient kakaoPayClient;
  private final KakaoPayProperties kakaoPayProperties;
  private final RedisTemplate<String, Object> redisTemplate;
  private final OrderService orderService;

  @Override
  protected OrderService getOrderService() {
    return orderService;
  }

  //외부 API요청
  public KakaoPayReadyResponse singlePayReadyRequest(String userId, Orders order) {

    KakaoPayReadyRequest kakaoPayReadyRequest = this.createKakaoPayReadyRequest(userId, order);

    KakaoPayReadyResponse kakaoPayReadyResponse = kakaoPayClient.requestReady(kakaoPayReadyRequest);

    // Redis에 response 상태 저장
    KakaoPayForApproval kakaoPayForApproval = KakaoPayForApproval
        .builder()
        .cid(kakaoPayReadyRequest.getCid())
        .tid(kakaoPayReadyResponse.getTid())
        .partnerOrderId(kakaoPayReadyRequest.getPartner_order_id())
        .partnerUserId(kakaoPayReadyRequest.getPartner_user_id())
        .build();

    redisTemplate.opsForValue()
        .set(KakaoPayForApproval.getRedisKey(userId), kakaoPayForApproval, Duration.ofMinutes(5));
    return kakaoPayReadyResponse;
  }

  public KakaoPayReadyRequest createKakaoPayReadyRequest(String userId, Orders order) {
    return KakaoPayReadyRequest
        .builder()
        .cid(kakaoPayProperties.getClientId())
        .partner_order_id(order.getCode())
        .partner_user_id(userId)
        .item_name(summarizeItemName(order.getOrderItems()))
        .quantity(KAKAO_PAY_FIX_QUANTITY)
        .total_amount(String.valueOf(this.toInteger(order.getTotalOrderPrice())))//orderItem의 모든 orderPrice를 계산해줘야함.
        .vat_amount(String.valueOf(calculateVatAmount(order.getTotalOrderPrice())))
        .tax_free_amount(ZERO)
        .approval_url(kakaoPayProperties.getApprovalUrl())
        .fail_url(kakaoPayProperties.getFailUrl())
        .cancel_url(kakaoPayProperties.getCancelUrl())
        .build();
  }

  private int toInteger(BigDecimal totalOrderPrice) {
    return totalOrderPrice.setScale(0, RoundingMode.DOWN).intValueExact();
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

  // 추상 클래스의 메서드를 재정의
  @Override
  protected String doRequestToExternalPay(String userId, Orders order) {
    return this.singlePayReadyRequest(userId, order).getTid();
  }

}
