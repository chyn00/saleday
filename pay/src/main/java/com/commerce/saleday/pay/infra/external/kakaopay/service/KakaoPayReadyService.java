package com.commerce.saleday.pay.infra.external.kakaopay.service;

import com.commerce.saleday.order.domain.order.model.OrderItem;
import com.commerce.saleday.order.domain.order.model.Orders;
import com.commerce.saleday.pay.common.utils.JwtUtilsStub;
import com.commerce.saleday.pay.domain.model.Payment;
import com.commerce.saleday.pay.infra.external.kakaopay.KakaoPayClient;
import com.commerce.saleday.pay.infra.external.kakaopay.KakaoPayProperties;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayForApproval;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayReadyRequest;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayReadyResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoPayReadyService {

  public static final String KAKAO_PAY_FIX_QUANTITY = "1";
  public static final String ZERO = "0";
  private final KakaoPayClient kakaoPayClient;
  private final KakaoPayProperties kakaoPayProperties;
  private final RedisTemplate<String, Object> redisTemplate;

  //외부 API요청
  public KakaoPayReadyResponse singlePayReadyRequest(String userId, Payment payment) {

    KakaoPayReadyRequest kakaoPayReadyRequest = this.createKakaoPayReadyRequest(userId, payment);

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

  //외부 API요청
  public String singlePayApproveRequest(String pgToken) {

    //todo: 실제의 경우 jwtUtils가 아닌 어노테이션을 활용하여, userId를 추출
    String userId = String.valueOf(JwtUtilsStub.getUserId());

    KakaoPayForApproval kakaoPayForApproval = (KakaoPayForApproval) redisTemplate.opsForValue()
        .get(KakaoPayForApproval.getRedisKey(userId));

    //todo: 공통 Exception handler 처리
    if (kakaoPayForApproval == null) {
      throw new RuntimeException("레디스에 저장된 값이 없습니다.");
    }

    return kakaoPayClient.requestApprove(kakaoPayForApproval.toKakaoPayApproveRequest(pgToken));
  }

  public KakaoPayReadyRequest createKakaoPayReadyRequest(String userId, Payment payment) {
    Orders order = payment.getOrder();
    return KakaoPayReadyRequest
        .builder()
        .cid(kakaoPayProperties.getClientId())
        .partner_order_id(order.getCode())
        .partner_user_id(userId)
        .item_name(summarizeItemName(order.getOrderItems()))
        .quantity(KAKAO_PAY_FIX_QUANTITY)
        .total_amount(String.valueOf(order.getTotalOrderPrice()))//orderItem의 모든 orderPrice를 계산해줘야함.
        .vat_amount(String.valueOf(calculateVatAmount(order.getTotalOrderPrice())))
        .tax_free_amount(ZERO)
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
