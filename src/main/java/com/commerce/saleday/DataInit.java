package com.commerce.saleday;

import com.commerce.saleday.application.service.item.ItemService;
import com.commerce.saleday.domain.stock.service.ItemStockDomainService;
import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.review.model.Review;
import com.commerce.saleday.domain.stock.model.ItemStock;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor//빈 등록이 Application class에 있다.(로컬만 설정해주기 위해 수동 빈 등록)
public class DataInit {
  private final ItemService itemService;
  private final ItemStockDomainService itemStockDomainService;

  //스프링 부트가 빈 세팅 외부 설정 등 모두 boot up 되고 나서, 실행되도록 개발
  @EventListener(ApplicationReadyEvent.class)
  public void initData() {

    String code = "1234";
    String name = "과자";
    String content = "달달한과자";
    double price = 10000;
    List<Review> reviews = new ArrayList<>();

    Item item = Item.create(code, name, content, price, reviews);

    ItemStock itemStock = ItemStock
        .builder()
        .item(item)
        .quantity(11)
        .build();

    //item세팅
    itemService.save(item);
    //item의 수량 세팅
    itemStockDomainService.setUpItemStock(itemStock);
  }
}
