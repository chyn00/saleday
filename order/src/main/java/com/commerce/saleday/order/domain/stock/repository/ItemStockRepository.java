package com.commerce.saleday.order.domain.stock.repository;

import com.commerce.saleday.order.domain.stock.model.ItemStock;

public interface ItemStockRepository {
  ItemStock findItemStockByItemCode(String itemCode);
  ItemStock save(ItemStock itemStock);
}
