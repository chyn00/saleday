package com.commerce.consumer.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

  @Bean
  public ConsumerFactory<String, Object> consumerFactory() {
    Map<String, Object> config = new HashMap<>();

    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    config.put(ConsumerConfig.GROUP_ID_CONFIG, "stock.decreased.consumer");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

    // listen batch 적용
    config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);

    // trust가 안되어서 문제
    config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.commerce.saleday.message.stock");

    return new DefaultKafkaConsumerFactory<>(config);
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(){
    // ConsumerFactory 설정 적용
    ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());

    // 배치 리스너 활성화
    factory.setBatchListener(true);

    // poll 꼭 배치 사이즈 안채워도, 최대 대기 시간
    factory.getContainerProperties().setPollTimeout(3000);

    return factory;
  }

}
