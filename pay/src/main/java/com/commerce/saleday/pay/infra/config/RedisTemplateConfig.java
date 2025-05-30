package com.commerce.saleday.pay.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*
 * RedisTemplate을 선택한 이유 (Spring Cache vs RedisTemplate 고민 정리)
 *
 * - Spring Cache는 '캐시' 본연의 목적(성능 최적화, 호출 최적화)에 가깝고,
 * - 반면 Payment의 PG 통신 과정에서 사용하는 값은 "임시 데이터"에 해당함
 *   (ex: Ready 요청 시 받은 tid 등을 승인 단계에서 잠시 보존해야 함)
 *
 * - 해당 값은 TTL이 짧고(5분), 값 자체를 직접 제어/삭제/갱신할 필요가 있음
 *   → 이처럼 "흐름 유지를 위한 상태 저장"에는 RedisTemplate이 더 적절함
 *
 * - 추후 정말 캐싱이 필요한 로직이 있다면,
 *   그 부분은 Spring Cache를 통해 분리된 책임으로 처리할 예정
 */
@Configuration
public class RedisTemplateConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key: 문자열 직렬화
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Value: JSON 직렬화 (GenericJackson2 - spring data redis 에서 지원하는 json 직렬화)
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        return template;
    }
}
