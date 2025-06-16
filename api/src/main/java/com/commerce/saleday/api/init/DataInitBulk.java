package com.commerce.saleday.api.init;

import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import com.commerce.saleday.item.domain.item.ItemCreateCommand;
import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.domain.review.model.Review;
import com.commerce.saleday.item.infra.redis.RedisItemIdGenerator;
import com.commerce.saleday.item.service.item.ItemService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor//빈 등록이 Application class에 있다.(로컬만 설정해주기 위해 수동 빈 등록)
public class DataInitBulk {

  private final ItemService itemService;

  //todo: item직접 생성 말고, command -> save 리팩토링 필요
  //스프링 부트가 빈 세팅 외부 설정 등 모두 boot up 되고 나서, 실행되도록 개발
  @EventListener(ApplicationReadyEvent.class)
  public void initData() {

    log.info("------data init start line -----");


    int total = 1000000;//일단 데이터 100만개 삽입
    List<ItemCreateCommand> itemsCommand = new ArrayList<>();

    for (long i = 2000; i <= total; i++) {
      String code = "code" + i;
      String name = "과자" + i;
      String content = "설명:" + i;
      BigDecimal price = BigDecimal.valueOf((Math.random() * 10000));
      List<Review> reviews = new ArrayList<>();

      ItemCreateCommand itemCreateCommand = ItemCreateCommand
          .builder()
          .code(code)
          .name(name)
          .content(content)
          .price(price)
          .discountType(DiscountType.NONE)
          .reviews(reviews)
          .build();
      itemsCommand.add(itemCreateCommand);
    }

    itemService.saveAll(itemsCommand);

    log.info("------data init end line -----");
  }
}
