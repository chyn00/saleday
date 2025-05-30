package com.commerce.saleday.pay.infra.external.kakaopay.service;

import com.commerce.saleday.pay.infra.external.kakaopay.KakaoPayClient;
import com.commerce.saleday.pay.infra.external.kakaopay.KakaoPayProperties;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayApproveRequest;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayReadyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakoPayApproveService {

  private final KakaoPayClient kakaoPayClient;
  private final KakaoPayProperties kakaoPayProperties;
  private final RedisTemplate<String, Object> redisTemplate;

  //카카오 페이 승인 API요청
//  public String singlePayApproveRequest(String pgToken) {
//
//    KakaoPayApproveRequest kakaoPayReadyRequest =
//        KakaoPayApproveRequest
//        .builder()
//            .cid(kakaoPayProperties.getClientId())
//            .tid() //결제 준비 응답에 포함되어있음
//            .partnerOrderId()
//            .partnerUserId()
//            .pgToken(pgToken)
//            .payload()
//            .totalAmount()
//        .build();
//
//    return kakaoPayClient.requestApprove(kakaoPayReadyRequest);
//  }
}
