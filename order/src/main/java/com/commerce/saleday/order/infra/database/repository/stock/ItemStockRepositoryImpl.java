package com.commerce.saleday.order.infra.database.repository.stock;

import com.commerce.saleday.item.domain.item.model.QItem;
import com.commerce.saleday.order.domain.stock.model.ItemStock;
import com.commerce.saleday.order.domain.stock.model.QItemStock;
import com.commerce.saleday.order.domain.stock.repository.ItemStockRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ItemStockRepositoryImpl implements ItemStockRepository {

  private final ItemStockJpaRepository itemStockJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

  public ItemStockRepositoryImpl(ItemStockJpaRepository itemStockJpaRepository,
      JPAQueryFactory jpaQueryFactory) {
    this.itemStockJpaRepository = itemStockJpaRepository;
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public ItemStock findItemStockByItemCode(String itemCode) {
    QItemStock itemStock = QItemStock.itemStock;
    QItem item = QItem.item;

    return Optional.ofNullable(
            jpaQueryFactory
                .selectFrom(itemStock)
                .join(itemStock.item)
                .fetchJoin()
                .where(item.code.eq(itemCode))
                .fetchOne()
        )
        .orElseThrow(() -> new EntityNotFoundException("상품 정보가 없습니다."));
  }

  @Override
  public ItemStock save(ItemStock itemStock) {
    return itemStockJpaRepository.save(itemStock);
  }
}
