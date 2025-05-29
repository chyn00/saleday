package com.commerce.saleday.pay.infra.external.kakaopay.service;

import com.commerce.saleday.pay.infra.external.kakaopay.KakaoPayClient;
import com.commerce.saleday.pay.infra.external.kakaopay.KakaoPayProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakoPayApproveService {

  private final KakaoPayClient kakaoPayClient;
  private final KakaoPayProperties kakaoPayProperties;

}
