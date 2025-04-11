package com.commerce.saleday.infra.database.repository.item;

import com.commerce.saleday.domain.item.model.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

//Jpa Data Repository 인터페이스
public interface ItemJpaRepository extends JpaRepository<Item, Long> {

  Optional<Item> findByCode(String itemCode);
  List<Item> findByCodeIn(List<String> itemCodeList);
}
