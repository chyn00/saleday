package com.commerce.saleday.infra.database.repository.stock;

import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.stock.model.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemStockJpaRepository extends JpaRepository<ItemStock, Long> {

  ItemStock item(Item item);
}
