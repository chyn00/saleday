package com.commerce.saleday.domain.stock.port;


import com.commerce.saleday.domain.stock.model.ItemStock;

public interface ItemStockKafkaPort {

  void publishDecreaseStock(ItemStock itemStock);
}
