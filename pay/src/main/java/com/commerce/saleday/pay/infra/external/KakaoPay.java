package com.commerce.saleday.pay.infra.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KakaoPay {

  private final RestClient restClient;

}
