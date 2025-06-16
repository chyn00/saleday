package com.commerce.saleday.api.init;

import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.domain.review.model.Review;
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

  //스프링 부트가 빈 세팅 외부 설정 등 모두 boot up 되고 나서, 실행되도록 개발
  @EventListener(ApplicationReadyEvent.class)
  public void initData() {

    log.info("------data init start line -----");

    int total = 1_002_000;
    int batchSize = 1000;
    List<Item> items = new ArrayList<>(batchSize);

    for (int i = 2000; i <= total; i++) {
      String code = "code" + i;
      String name = "과자" + i;
      String content = "설명" + i;
      BigDecimal price = BigDecimal.valueOf((Math.random() * 10000));
      List<Review> reviews = new ArrayList<>();

      Item item = Item.create(code, name, content, price, reviews);
      items.add(item);

      //메모리를 효율적으로 사용하기 위해 1000마다 list reset
      if (i % batchSize == 0) {
        itemService.saveAll(items);
        items.clear();// 명시적으로 객체 리셋
      }
    }

    log.info("------data init end line -----");
  }
}
