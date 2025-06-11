package com.commerce.saleday.item.domain.item.repository;

import com.commerce.saleday.item.domain.item.model.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepository {

  Optional<Item> findItemByCode(String itemCode);

  List<Item> findItemsByCode(List<String> itemCode);

  Item save(Item item);

  Page<Item> findByCodeContains(String code, Pageable pageable);
}
