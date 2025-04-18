package com.commerce.saleday;

import com.commerce.saleday.application.service.item.ItemService;
import com.commerce.saleday.application.service.stock.ItemStockService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SaledayApplication {

  public static void main(String[] args) {
    SpringApplication.run(SaledayApplication.class, args);
  }

  @Bean
  @Profile("local")//수동 등록시에는 주입이 필요함, 로컬 환경일떄만 사용을 위해 이렇게 구성
  public DataInit testDataInit(ItemService itemService, ItemStockService itemStockService) {
    return new DataInit(itemService, itemStockService);
  }
}
