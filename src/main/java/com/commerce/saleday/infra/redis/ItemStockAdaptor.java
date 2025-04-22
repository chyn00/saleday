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

  @Override//레디스 싱글 쓰레드 세팅(세팅할때 동시성 문제 있을 수 있어, 분산락 필요)
  public void setInitialItemStock(String itemCode, Long quantity) {
    redisTemplate.opsForValue().set(itemCode, quantity.toString());
  }

  @Override
  public String getItemStock(String itemCode) {
    return redisTemplate.opsForValue().get(itemCode);
  }
}
