package com.commerce.saleday.api.presentation.pay;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoPayCallbackController {

  //단건 주문 조회
  @GetMapping("/success")
  public String getOrder(@RequestParam String pg_token) {

    return "콜백 테스트 : " + pg_token;
  }
}
