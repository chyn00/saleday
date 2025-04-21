package com.commerce.saleday.infra.redis;

import com.commerce.saleday.domain.stock.port.ItemStockPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemStockAdaptor implements ItemStockPort {

  private final RedisTemplate<String, String> redisTemplate;

  @Override//레디스 싱글 쓰레드 감소
  public Long decrementItemStock(String itemCode) {
    return redisTemplate.opsForValue().decrement(itemCode);
  }

  @Override//레디스 싱글 쓰레드 감소
  public Long incrementItemStock(String itemCode) {
    return redisTemplate.opsForValue().increment(itemCode);
  }
}
