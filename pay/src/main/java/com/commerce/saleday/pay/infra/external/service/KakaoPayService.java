package com.commerce.saleday.pay.infra.external.service;

import com.commerce.saleday.order.domain.order.model.OrderItem;
import com.commerce.saleday.order.domain.order.model.Orders;
import com.commerce.saleday.pay.domain.model.Payment;
import com.commerce.saleday.pay.infra.external.model.KakaoPayReadyRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class KakaoPayService {

  private final RestClient restClient;
  private String kakaopayUrl;
  private String kakaopayClientId;
  private String kakaopaySecretKey;
  private String approvalUrl;
  private String failUrl;
  private String cancelUrl;

  //yaml 설정 때문에 직접 구현
  public KakaoPayService(@Value("${external.kakaopay.url}") String kakaopayUrl,
      @Value("${external.kakaopay.headers.client-id}") String kakaopayClientId,
      @Value("${external.kakaopay.headers.client-secret-key}") String kakaopaySecretKey,
      @Value("${internal.url.approval}") String approvalUrl,
      @Value("${internal.url.fail}") String failUrl,
      @Value("${internal.url.cancel}") String cancelUrl) {

    this.restClient = RestClient.create();
    this.kakaopayUrl = kakaopayUrl;
    this.kakaopayClientId = kakaopayClientId;
    this.kakaopaySecretKey = kakaopaySecretKey;
    this.approvalUrl = approvalUrl;
    this.failUrl = failUrl;
    this.cancelUrl = cancelUrl;
  }

  //외부 API요청
  public String singleRequestToKakaoPay(Payment payment) {

    Orders order = payment.getOrder();
    KakaoPayReadyRequest
        .builder()
        .cid("TC0ONETIME")
        .partner_order_id(order.getCode())
        .partner_user_id("UserId")
        .item_name(summarizeItemName(order.getOrderItems()))
        .quantity("1")
        .total_amount(order.getTotalOrderPrice().toString())//orderItem의 모든 orderPrice를 계산해줘야함.
        .vat_amount(calculateVatAmount(order.getTotalOrderPrice()).toString())
        .tax_free_amount("0")
        .approval_url(approvalUrl)
        .fail_url(failUrl)
        .cancel_url(cancelUrl)
        .build();

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("cid", "TC0ONETIME");
    requestBody.put("partner_order_id", "partner_order_id");
    requestBody.put("partner_user_id", "partner_user_id");
    requestBody.put("item_name", "초코파이");
    requestBody.put("quantity", "1");
    requestBody.put("total_amount", "2200");
    requestBody.put("vat_amount", "200");
    requestBody.put("tax_free_amount", "0");
    requestBody.put("approval_url", approvalUrl);
    requestBody.put("fail_url", failUrl);
    requestBody.put("cancel_url", cancelUrl);

    return restClient.post()
        .uri(kakaopayUrl + "/v1/payment/ready")
        .header(HttpHeaders.AUTHORIZATION, kakaopaySecretKey)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(requestBody)
        .retrieve()
        .body(String.class);
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
