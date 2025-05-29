package com.commerce.saleday.pay.infra.external.kakaopay;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class KakaoPayProperties {

  private final String url;
  private final String clientId;
  private final String clientSecretKey;
  private final String approvalUrl;
  private final String failUrl;
  private final String cancelUrl;

  public KakaoPayProperties(
      @Value("${external.kakao-pay.url}") String url,
      @Value("${external.kakao-pay.headers.client-id}") String clientId,
      @Value("${external.kakao-pay.headers.client-secret-key}") String clientSecretKey,
      @Value("${external.kakao-pay.redirect.approval}") String approvalUrl,
      @Value("${external.kakao-pay.redirect.fail}") String failUrl,
      @Value("${external.kakao-pay.redirect.cancel}") String cancelUrl
  ) {
    this.url = url;
    this.clientId = clientId;
    this.clientSecretKey = clientSecretKey;
    this.approvalUrl = approvalUrl;
    this.failUrl = failUrl;
    this.cancelUrl = cancelUrl;
  }
}
