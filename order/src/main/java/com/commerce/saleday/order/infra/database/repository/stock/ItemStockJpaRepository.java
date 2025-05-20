package com.commerce.saleday.order.infra.database.repository.stock;

import com.commerce.saleday.order.domain.item.model.Item;
import com.commerce.saleday.order.domain.stock.model.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemStockJpaRepository extends JpaRepository<ItemStock, Long> {

  ItemStock item(Item item);
}
