package com.commerce.saleday.api.controller.internal.pay.controller;

import com.commerce.saleday.pay.infra.external.kakaopay.service.KakaoPayApproveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoPayCallbackController {

  private final KakaoPayApproveService kakaoPayApproveService;

  //단건 결제 콜백 redirect(일단 kakaoPay 등록된 url대로)
  @GetMapping("/success")
  public String getOrder(@RequestParam String pg_token) {

    return kakaoPayApproveService.singlePayApproveRequest(pg_token);
  }
}
