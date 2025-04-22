package com.commerce.saleday.application.service.orchestrator;

import com.commerce.saleday.application.service.item.ItemService;
import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.review.model.Review;
import com.commerce.saleday.domain.stock.model.ItemStock;
import jakarta.transaction.Transactional;
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
    String code = "1235";
    String name = "테스트과자";
    String content = "테스트달달한과자";
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

    //itemStock 세팅
    itemStockOrchestratorService.saveItemWithRLock("1235", itemStock);
    log.info("–--------test end line----------------");
  }
}