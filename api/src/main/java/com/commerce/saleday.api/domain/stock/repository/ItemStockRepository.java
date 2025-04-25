package com.commerce.saleday.api.domain.stock.repository;

import com.commerce.saleday.api.domain.stock.model.ItemStock;

public interface ItemStockRepository {
  ItemStock findItemStockByItemCode(String itemCode);

  ItemStock save(ItemStock itemStock);
}
