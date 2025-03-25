package com.commerce.saleday.domain.item.repository;

import com.commerce.saleday.domain.item.model.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository {
    Item findItemByCode(String itemCode);
}
