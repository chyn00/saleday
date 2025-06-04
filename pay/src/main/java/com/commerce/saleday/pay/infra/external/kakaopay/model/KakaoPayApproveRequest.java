package com.commerce.saleday.pay.infra.external.kakaopay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoPayApproveRequest {

  @JsonProperty("cid")
  private String cid; // 가맹점 코드 (필수)

  @JsonProperty("tid")
  private String tid; // 결제 고유 번호 (필수)

  @JsonProperty("partner_order_id")
  private String partnerOrderId; // 가맹점 주문번호 (필수)

  @JsonProperty("partner_user_id")
  private String partnerUserId; // 가맹점 회원 ID (필수)

  @JsonProperty("pg_token")
  private String pgToken; // 결제 승인 토큰 (필수)

  @JsonProperty("cid_secret")
  private String cidSecret; // 가맹점 코드 인증키 (선택)

  @JsonProperty("payload")
  private String payload; // 저장하고 싶은 값 (선택, 최대 200자)

  @JsonProperty("total_amount")
  private Integer totalAmount; // 총 결제 금액 (선택)
}
