//package com.commerce.saleday.order;
//
//import com.commerce.saleday.common.jpa.config.QuerydslConfig;
//import com.commerce.saleday.order.service.ItemService;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//
//@EnableJpaAuditing
//@SpringBootApplication
//@Import(QuerydslConfig.class)// 같은 queryDslConfig쓰고 있음을 명시
//public class OrderApplication {
//
//  public static void main(String[] args) {
//    SpringApplication.run(OrderApplication.class, args);
//  }
//
//  @Bean
//  @Profile("local")//수동 등록시에는 주입이 필요함, 로컬 환경일떄만 사용을 위해 이렇게 구성
//  public DataInitForOrderTest dataInitForOrderTest(ItemService itemService) {
//    return new DataInitForOrderTest(itemService);
//  }
//}
