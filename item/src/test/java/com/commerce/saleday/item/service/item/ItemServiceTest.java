package com.commerce.saleday.item.service.item;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.commerce.saleday.common.exception.SaleDayException;
import com.commerce.saleday.item.domain.item.ItemCreateCommand;
import com.commerce.saleday.item.domain.item.repository.ItemRepository;
import com.commerce.saleday.item.domain.item.repository.ItemReviewRepository;
import com.commerce.saleday.item.infra.redis.RedisItemIdGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

  @Mock
  private ItemRepository itemRepository;

  @Mock
  private ItemReviewRepository itemReviewRepository;

  @Mock
  private RedisItemIdGenerator redisItemIdGenerator;

  @InjectMocks
  private ItemService itemService;

  @Test
  @DisplayName("bulk 저장 시 1000개 단위로 persistAll을 분할 호출한다")
  void saveAll_partitionsInBatchSize() {
    List<ItemCreateCommand> commands = new ArrayList<>();
    for (int i = 0; i < 1001; i++) {
      commands.add(ItemCreateCommand.builder()
          .code("ITEM-" + i)
          .name("name-" + i)
          .content("content")
          .price(BigDecimal.valueOf(1000))
          .reviews(new ArrayList<>())
          .build());
    }

    when(redisItemIdGenerator.generateBatchAndGetStartId(ItemService.BATCH_SIZE)).thenReturn(1L);

    itemService.saveAll(commands);

    verify(itemRepository, times(2)).persistAll(anyList());
    verify(redisItemIdGenerator).generateBatchAndGetStartId(ItemService.BATCH_SIZE);
  }

  @Test
  @DisplayName("검색 code가 공백이면 예외를 던진다")
  void getItemsByCodeContaining_throws_whenCodeBlank() {
    assertThatThrownBy(() -> itemService.getItemsByCodeContaining(" ", org.springframework.data.domain.Pageable.unpaged()))
        .isInstanceOf(SaleDayException.class);
  }
}
