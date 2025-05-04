package com.commerce.saleday.domain.stock.repository;

import com.commerce.saleday.domain.stock.model.ItemStock;

public interface ItemStockRepository {
  ItemStock findItemStockByItemCode(String itemCode);
  ItemStock save(ItemStock itemStock);
}
