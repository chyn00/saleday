package com.commerce.saleday.domain.stock.port;


import com.commerce.saleday.message.stock.DecreaseStockEvent;

public interface ItemStockConsumerKafkaPort {

  public void decreaseStockListener(DecreaseStockEvent decreaseStockEvent);
}
