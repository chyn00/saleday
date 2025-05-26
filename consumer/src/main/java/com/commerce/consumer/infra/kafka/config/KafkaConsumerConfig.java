package com.commerce.consumer.infra.kafka.config;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

  private final KafkaTemplate<String, Object> kafkaTemplate;

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

    // 에러 난 경우 DLQ
    factory.setCommonErrorHandler(createErrorHandler(kafkaTemplate));

    // poll 꼭 배치 사이즈 안채워도, 최대 대기 시간
    factory.getContainerProperties().setPollTimeout(3000);

    return factory;
  }

  //dlq 구현
  private DefaultErrorHandler createErrorHandler(KafkaTemplate<String, Object> kafkaTemplate) {

    //partition 은 카프카의 partition 을 의미하며, 순서 보장(파티션별로)을 위해 파티션을 지정해서 전송한다.
    DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
        kafkaTemplate,
        (record, ex) -> new TopicPartition("stock.decreased.deadletter", record.partition())
    );

    //1, 2, 4초로 늘려가며 대응
    ExponentialBackOff backOff = new ExponentialBackOff(1000L, 2.0);
    backOff.setMaxElapsedTime(7000L);//7초 이내로 해결

    return new DefaultErrorHandler(recoverer, backOff);
  }

}
