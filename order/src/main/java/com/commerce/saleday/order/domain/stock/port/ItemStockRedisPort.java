package com.commerce.saleday.order.domain.stock.port;

public interface ItemStockRedisPort {
    Long decrementItemStock(String itemCode);
    Long incrementItemStock(String itemCode);
    void setInitialItemStock(String itemCode, Long quantity);
    String getItemStock(String itemCode);
}
