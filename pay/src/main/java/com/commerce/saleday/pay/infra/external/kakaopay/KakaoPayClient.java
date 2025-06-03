package com.commerce.saleday.pay.infra.external.kakaopay;

import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayApproveRequest;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayReadyRequest;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayReadyResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Getter
@Component
@RequiredArgsConstructor
public class KakaoPayClient {

  private final RestClient restClient;
  private final KakaoPayProperties kakaoPayProperties;

  public KakaoPayReadyResponse requestReady(KakaoPayReadyRequest kakaoPayReadyRequest){
    return restClient.post()
        .uri(kakaoPayProperties.getUrl() + "/v1/payment/ready")
        .header(HttpHeaders.AUTHORIZATION, kakaoPayProperties.getClientSecretKey())
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(kakaoPayReadyRequest)
        .retrieve()
        .body(KakaoPayReadyResponse.class);
  }

  public String requestApprove(KakaoPayApproveRequest kakaoPayApproveRequest){
    return restClient.post()
        .uri(kakaoPayProperties.getUrl() + "/v1/payment/approve")
        .header(HttpHeaders.AUTHORIZATION, kakaoPayProperties.getClientSecretKey())
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(kakaoPayApproveRequest)
        .retrieve()
        .body(String.class);
  }

}
