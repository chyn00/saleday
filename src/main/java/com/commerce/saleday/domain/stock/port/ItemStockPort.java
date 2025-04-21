package com.commerce.saleday.domain.stock.port;

public interface ItemStockPort {
    Long decrementItemStock(String itemCode);
    Long incrementItemStock(String itemCode);
}
