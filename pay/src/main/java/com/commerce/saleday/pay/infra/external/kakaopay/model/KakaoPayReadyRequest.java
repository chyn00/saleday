package com.commerce.saleday.pay.infra.external.kakaopay.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoPayReadyRequest {

  public final static String KAKAO_PAY_READY = "kakao-pay-ready:";
  private final String cid;
  private final String partner_order_id;
  private final String partner_user_id;
  private final String item_name;
  private final String quantity;
  private final String total_amount;
  private final String vat_amount;
  private final String tax_free_amount;
  private final String approval_url;
  private final String fail_url;
  private final String cancel_url;

  public static String getRedisKey(String userId){
    return KAKAO_PAY_READY + userId;
  }
}
