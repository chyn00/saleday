package com.commerce.saleday.api.infra.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncExecutorsConfig {

  /**
   * Kafka publish trigger용 Executor
   * - 목적: (선택) HTTP 요청 스레드(Tomcat)와 발행 트리거 로직을 격리
   * - send() 자체는 내부적으로 비동기이지만, 호출/전처리/로깅이 커질 때 유용
   */
  @Bean(name = "kafkaPublishExecutor")
  public Executor kafkaPublishExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("kafka-pub-");
    executor.setCorePoolSize(8);
    executor.setMaxPoolSize(16);
    executor.setQueueCapacity(2000);
    executor.setKeepAliveSeconds(30);
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(10);
    // 큐가 꽉 차면 호출한 쪽에서 실행 → 자연스러운 backpressure
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.initialize();
    return executor;
  }

  /**
   * Kafka send callback -> OutboxStatus DB 업데이트용 Executor
   * - 목적: Kafka callback thread 보호 + DB 부하 제한(벌크헤드)
   * - DB 커넥션 풀(Hikari)보다 과하게 키우지 않는 걸 권장
   */
  @Bean(name = "outboxCallbackExecutor")
  public Executor outboxCallbackExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("outbox-cb-");
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(8);
    executor.setQueueCapacity(1000);
    executor.setKeepAliveSeconds(30);
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(10);
    // DB 업데이트가 밀리면 시스템이 무너질 수 있으니, 강한 backpressure(또는 드랍) 중 택1
    // 1) backpressure: CallerRunsPolicy (안전하지만 caller가 Kafka callback thread면 주의)
    // 2) drop: AbortPolicy (명확히 실패 처리 + 리트라이/스케줄러로 회수)
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    executor.initialize();
    return executor;
  }
}
