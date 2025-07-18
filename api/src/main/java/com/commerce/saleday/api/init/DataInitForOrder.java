package com.commerce.saleday.api.init;

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
public class DataInitForOrder {
  private final ItemService itemService;
  private final ItemStockOrchestratorService itemStockOrchestratorService;

  //todo: item직접 생성 말고, command -> save 리팩토링 필요
  //스프링 부트가 빈 세팅 외부 설정 등 모두 boot up 되고 나서, 실행되도록 개발
  @EventListener(ApplicationReadyEvent.class)
  public void initData() {

    log.info("------data init start line -----");
    String code = "1234";
    String name = "과자";
    String content = "달달한과자";
    BigDecimal price = BigDecimal.valueOf(10000);
    List<Review> reviews = new ArrayList<>();

    Item item = Item.create(1L, code, name, content, price, reviews);

    ItemStock itemStock = ItemStock
        .builder()
        .item(item)
        .quantity(1000000)
        .build();

    // item세팅
    itemService.save(item);

    // item 수량 세팅
    itemStockOrchestratorService.saveItemStockWithRLock(code, itemStock);


    String code1 = "1238";
    String name1 = "테스트용 과자 2";
    String content1 = "테스트용 과자 2";
    BigDecimal price1 = BigDecimal.valueOf(2000);
    List<Review> reviews1 = new ArrayList<>();

    Item item1 = Item.create(2L, code1, name1, content1, price1, reviews1);

    //item세팅
    itemService.save(item1);
    String code2 = "12341";
    String name2 = "테스트용 과자 3";
    String content2 = "테스트용 과자 3";
    BigDecimal price2 = BigDecimal.valueOf(210);
    List<Review> reviews2 = new ArrayList<>();

    Item item2 = Item.create(3L, code2, name2, content2, price2, reviews2);

    //item세팅
    itemService.save(item2);

    log.info("------data init end line -----");
  }
}
