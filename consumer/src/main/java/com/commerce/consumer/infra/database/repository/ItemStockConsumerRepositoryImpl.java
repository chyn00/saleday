package com.commerce.consumer.infra.database.repository;

import com.commerce.saleday.domain.item.model.QItem;
import com.commerce.saleday.domain.stock.model.ItemStock;
import com.commerce.saleday.domain.stock.model.QItemStock;
import com.commerce.saleday.domain.stock.repository.ItemStockConsumerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

//api와 중복되더라도 추후 다를 가능성 있으므로 일단 구현체 분리하여 개발
@Repository
@RequiredArgsConstructor
public class ItemStockConsumerRepositoryImpl implements ItemStockConsumerRepository {

  private final ItemStockConsumerJpaRepository itemStockJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<ItemStock> findItemStocksByItemCodes(List<String> itemCodes) {
    QItemStock itemStock = QItemStock.itemStock;
    QItem item = QItem.item;

    return jpaQueryFactory
        .selectFrom(itemStock)
        .join(itemStock.item, item)
        .fetchJoin()
        .where(item.code.in(itemCodes))
        .fetch();
  }

  @Override
  public void flush() {
    itemStockJpaRepository.flush();
  }
}
