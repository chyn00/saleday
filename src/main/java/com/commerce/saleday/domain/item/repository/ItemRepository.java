package com.commerce.saleday.domain.item.repository;

import com.commerce.saleday.domain.item.model.Item;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository {
    Optional<Item> findItemByCode(String itemCode);
}
