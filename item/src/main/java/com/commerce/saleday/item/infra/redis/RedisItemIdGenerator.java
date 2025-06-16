package com.commerce.saleday.item.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisItemIdGenerator {

    private static final String ITEM_ID_KEY = "item:id:sequence";
    private final RedisTemplate<String, String> redisTemplate;

    public Long generateId() {
        return redisTemplate.opsForValue().increment(ITEM_ID_KEY);
    }

    //정합성 때문에 generated 와 get 함께 사용
    //처음 시작 index를 활용하여, batchSize만큼 id를 저장
    public Long generateBatchAndGetStartId(long batchSize) {
        Long current = redisTemplate.opsForValue().increment(ITEM_ID_KEY, batchSize);
      return current - batchSize + 1;
    }
}