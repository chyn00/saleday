package com.commerce.saleday.pay.infra.external.kakaopay;

import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayApproveRequest;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayReadyRequest;
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

  public String requestReady(KakaoPayReadyRequest kakaoPayReadyRequest){
    return restClient.post()
        .uri(kakaoPayProperties.getUrl() + "/v1/payment/ready")
        .header(HttpHeaders.AUTHORIZATION, kakaoPayProperties.getClientSecretKey())
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(kakaoPayReadyRequest)
        .retrieve()
        .body(String.class);
  }

  //todo: id랑 device token을 기반으로 한 분산락 필요. 및 id, device token을 활용해서, 아이디와 트랜잭션 1대1 매핑
  //todo: 흐름 시퀀스 다이어그램 추가 필요
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
