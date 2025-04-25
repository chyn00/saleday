package com.commerce.saleday.api.domain.stock.port;

public interface ItemStockPort {
    Long decrementItemStock(String itemCode);
    Long incrementItemStock(String itemCode);
    void setInitialItemStock(String itemCode, Long quantity);
    String getItemStock(String itemCode);
}
