package com.commerce.saleday.pay.infra.external.kakaopay.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoPayForApproval {

  private String cid; // 가맹점 코드 (필수)
  private String tid; // 결제 고유 번호 (필수)
  private String partnerOrderId; // 가맹점 주문번호 (필수)
  private String partnerUserId; // 가맹점 회원 ID (필수)

  private Integer totalAmount; // 총 결제 금액

  public static String getRedisKey(String userId) {
    return "kakao-pay-approve:" + userId;
  }

  public KakaoPayApproveRequest toKakaoPayApproveRequest(String pgToken) {
    return KakaoPayApproveRequest
        .builder()
        .cid(this.cid)
        .tid(this.tid)
        .partnerOrderId(this.partnerOrderId)
        .partnerUserId(this.partnerUserId)
        .pgToken(pgToken)
        .cidSecret("NONE")
        .payload("결제정보는 주문 내역을 참조해주세요.")
        .totalAmount(this.totalAmount)
        .build();
  }
}
