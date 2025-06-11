package com.commerce.saleday.api;

import com.commerce.saleday.api.service.orchestrator.ItemStockOrchestratorService;

import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.domain.review.model.Review;
import com.commerce.saleday.item.service.item.ItemService;
import com.commerce.saleday.order.domain.stock.model.ItemStock;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor//빈 등록이 Application class에 있다.(로컬만 설정해주기 위해 수동 빈 등록)
public class DataInit {
  private final ItemService itemService;
  private final ItemStockOrchestratorService itemStockOrchestratorService;

  //스프링 부트가 빈 세팅 외부 설정 등 모두 boot up 되고 나서, 실행되도록 개발
  @EventListener(ApplicationReadyEvent.class)
  public void initData() {

    log.info("------data init start line -----");
    String code = "1235";
    String name = "1235과자";
    String content = "1235과자는 맛있어요";
    BigDecimal price = BigDecimal.valueOf(5000);
    List<Review> reviews = new ArrayList<>();

    Item item = Item.create(code, name, content, price, reviews);

    ItemStock itemStock = ItemStock
        .builder()
        .item(item)
        .quantity(100)
        .build();

    //item세팅
    itemService.save(item);
    //item의 수량 세팅
    itemStockOrchestratorService.saveItemWithRLock(code, itemStock);
    log.info("------data init end line -----");
  }
}
