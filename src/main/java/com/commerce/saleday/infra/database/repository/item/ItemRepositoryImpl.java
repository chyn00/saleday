package com.commerce.saleday.infra.database.repository.item;

import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.item.repository.ItemRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

//Jpa Data Repository를 활용하여, 인프라스트럭쳐 레이어에서의 JPA 종속적 로직(도메인에서 종속성을 떼어내기 위해 이렇게 사용)
//필요하면 쿼리 DSL도 이 구현체에 만들어 놓는다.
@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

  private final ItemJpaRepository itemJpaRepository;

  @Override
  public Optional<Item> findItemByCode(String itemCode) {
    return itemJpaRepository.findByCode(itemCode);
  }

  @Override
  public List<Item> findItemsByCode(List<String> itemCodeList) {
    return itemJpaRepository.findByCodeIn(itemCodeList);
  }

  @Override
  public Item save(Item item) {
    return itemJpaRepository.save(item);
  }
}
