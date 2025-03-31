package com.commerce.saleday.infra.database.repository.item;

import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.item.repository.ItemRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

//Jpa Data Repository를 활용하여, 인프라스트럭쳐 레이어에서의 JPA 종속적 로직(도메인에서 종속성을 떼어내기 위해 이렇게 사용)
@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

  private final ItemJpaRepository itemJpaRepository;

  @Override
  public Optional<Item> findItemByCode(String itemCode) {
    return itemJpaRepository.findByCode(itemCode);
  }
}
