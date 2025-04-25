package com.commerce.saleday.api.infra.database.repository.stock;

import com.commerce.saleday.api.domain.item.model.Item;
import com.commerce.saleday.api.domain.stock.model.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemStockJpaRepository extends JpaRepository<ItemStock, Long> {

  ItemStock item(Item item);
}
