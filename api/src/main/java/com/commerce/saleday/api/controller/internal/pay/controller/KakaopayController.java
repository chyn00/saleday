package com.commerce.saleday.api.controller.internal.pay.controller;

import com.commerce.saleday.api.controller.internal.pay.model.ExternalPayRequest;
import com.commerce.saleday.pay.infra.external.kakaopay.service.KakaoPayReadyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaopayController {

  private final KakaoPayReadyService kakaoPayReadyService;

  //모든걸 추상화하기보다, external 과 관련있는 api는 분리해서 설계한다.
  @PostMapping("/kakao-pay/pay/ready")
  public String payUsingKakaoPay(@RequestBody @Valid ExternalPayRequest externalPayRequest) {

    return kakaoPayReadyService.pay(externalPayRequest.getUserId(),
        externalPayRequest.getOrderCode());
  }
}
