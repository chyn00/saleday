package com.commerce.saleday.domain.stock.port;

public interface ItemStockPort {
    Long decrementItemStock(String itemCode);
    Long incrementItemStock(String itemCode);
    void setInitialItemStock(String itemCode, Long quantity);
    Long getItemStock(String itemCode);
}
