package com.commerce.saleday.api.domain.item.repository;

import com.commerce.saleday.api.domain.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {

  Optional<Item> findItemByCode(String itemCode);

  List<Item> findItemsByCode(List<String> itemCode);

  Item save(Item item);
}
