package com.commerce.saleday.infra.database.repository.item;

import com.commerce.saleday.domain.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

//Jpa Data Repository 인터페이스
public interface ItemJpaRepository extends JpaRepository<Item,Long> {
    Item findByCode(String itemCode);
}
