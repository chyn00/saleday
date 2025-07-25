package com.commerce.saleday.api.infra.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

  @Bean
  public ProducerFactory<String, Object> producerFactory() {
    Map<String, Object> config = new HashMap<>();

    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    // 데이터 정합성을 높이기 위해 카프카 설정 추가(producer -> broker)
    // todo: ack를 활용하여, produce 쪽 at least once 보장, consumer는 멱등성 필요하여 재설계 필요
    config.put(ProducerConfig.ACKS_CONFIG, "all"); // 가장 강력한 ack 설정
    config.put(ProducerConfig.RETRIES_CONFIG, 3); // 재시도 횟수
    config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // 멱등성 보장
//    config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 3000); // 전송 타임아웃
    return new DefaultKafkaProducerFactory<>(config);
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public NewTopic stockDecreasedTopic() {
    return new NewTopic("stock.decreased", 3, (short) 1);
  }


}
