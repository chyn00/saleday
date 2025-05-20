package com.commerce.saleday.order.domain.stock.port;


import com.commerce.saleday.message.stock.DecreaseStockEvent;
import java.util.List;

public interface ItemStockConsumerKafkaPort {

  void decreaseStockListener(List<DecreaseStockEvent> decreaseStockEvent);
}
