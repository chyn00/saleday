package com.commerce.saleday.application.service.orchestrator;

import com.commerce.saleday.application.service.item.ItemService;
import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.stock.model.ItemStock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemStockOrchestratorServiceTest {

  @Autowired
  ItemStockOrchestratorService itemStockOrchestratorService;

  @Autowired
  ItemService itemService;

  @Test
  void saveItemWithRLock() {

    Item item = itemService.getItem("1234");

    ItemStock itemStock = ItemStock
        .builder()
        .item(item)
        .quantity(11)
        .build();

    itemStockOrchestratorService.saveItemWithRLock("1234", itemStock);
  }
}