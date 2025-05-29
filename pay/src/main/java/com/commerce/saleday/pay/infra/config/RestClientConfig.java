package com.commerce.saleday.pay.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

//주석 출처 : https://stackoverflow.com/questions/39174669
@Configuration//프록시를 활용해 등록된 bean의 싱글톤을 보장해줌. component 만 사용한 경우, new 로직은 계속 새 객체
public class RestClientConfig {

  @Bean
  public RestClient restClient() {
    return RestClient.create();
  }

}
