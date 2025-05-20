package com.commerce.saleday.order.domain.stock.repository;

import com.commerce.saleday.order.domain.stock.model.ItemStock;
import java.util.List;

public interface ItemStockConsumerRepository {
  List<ItemStock> findItemStocksByItemCodes(List<String> itemCode);
  void flush();
}
