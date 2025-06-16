package com.commerce.saleday.api.service.orchestrator;

import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.domain.review.model.Review;
import com.commerce.saleday.item.service.item.ItemService;
import com.commerce.saleday.order.domain.stock.model.ItemStock;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@Transactional
class ItemStockOrchestratorServiceTest {

  @Autowired
  ItemStockOrchestratorService itemStockOrchestratorService;

  @Autowired
  ItemService itemService;

  @Test
  void saveItemWithRLock() {

    log.info("–--------test start line----------------");
    String code = "1236";
    String name = "추가과자";
    String content = "추가된과자에요";
    BigDecimal price = BigDecimal.valueOf(3000);
    List<Review> reviews = new ArrayList<>();

    Item item = Item.create(5L, code, name, content, price, reviews);

    ItemStock itemStock = ItemStock
        .builder()
        .item(item)
        .quantity(11)
        .build();

    //item세팅
    itemService.save(item);

    //itemStock 세팅
    itemStockOrchestratorService.saveItemWithRLock("1236", itemStock);
    log.info("–--------test end line----------------");
  }
}