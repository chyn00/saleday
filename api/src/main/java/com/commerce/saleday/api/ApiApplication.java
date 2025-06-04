package com.commerce.saleday.api;

import com.commerce.saleday.order.service.ItemService;
import com.commerce.saleday.api.service.orchestrator.ItemStockOrchestratorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackages = "com.commerce.saleday") // todo: 하위 전체 커버되게 설정(나중에 분리 필요.)
@EnableJpaRepositories(basePackages = {"com.commerce.saleday"})
@EntityScan(basePackages = "com.commerce.saleday")
public class ApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }

  @Bean
  @Profile("local")//수동 등록시에는 주입이 필요함, 로컬 환경일떄만 사용을 위해 이렇게 구성
  public DataInit testDataInit(ItemService itemService,
      ItemStockOrchestratorService itemStockOrchestratorService) {
    return new DataInit(itemService, itemStockOrchestratorService);
  }

  @Bean
  @Profile("local")//수동 등록시에는 주입이 필요함, 로컬 환경일떄만 사용을 위해 이렇게 구성
  public DataInitForOrderTest dataInitForOrderTest(ItemService itemService) {
    return new DataInitForOrderTest(itemService);
  }
}
