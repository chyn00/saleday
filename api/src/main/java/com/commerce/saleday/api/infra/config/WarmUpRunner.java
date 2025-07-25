package com.commerce.saleday.api.infra.config;

import com.commerce.saleday.api.controller.order.model.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarmUpRunner {

    @EventListener(ApplicationReadyEvent.class)
    public void warmUp() {
        log.info("Warm-up process started...");
        int warmUpCount = 0;
        RestClient restClient = RestClient.builder().baseUrl("http://localhost:9999").build();
        for (int i = 0; i < 200; i++) {
            try {
                restClient.post()
                    .uri("/order/stock/limit")
                    .body(OrderRequestDto.builder().build())
                    .retrieve()
                    .toBodilessEntity(); // 응답 바디 없음
            } catch (Exception e) {
                // 실패해도 무시 (예열용)
            }
            warmUpCount++;
        }
        log.info("Warm-up process finished. warmUpCount requests: {}/200", warmUpCount);
    }
}
