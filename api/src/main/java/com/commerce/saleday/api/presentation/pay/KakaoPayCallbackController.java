package com.commerce.saleday.api.presentation.pay;

import com.commerce.saleday.pay.infra.external.kakaopay.service.KakaoPayReadyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoPayCallbackController {

  private final KakaoPayReadyService kakaoPayReadyService;
  //단건 결제 콜백 redirect
  //todo: pay 달아서 rest api 명, kakao developers 등록 내용 수정
  @GetMapping("/success")
  public String getOrder(@RequestParam String pg_token) {

    return kakaoPayReadyService.singlePayApproveRequest(pg_token);
  }
}
