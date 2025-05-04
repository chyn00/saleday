package com.commerce.consumer.infra.database.repository;

import com.commerce.saleday.domain.stock.model.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemStockConsumerJpaRepository extends JpaRepository<ItemStock, Long> {

}

