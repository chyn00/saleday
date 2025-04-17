package com.commerce.saleday.infra.database.repository.stock;

import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.item.model.QItem;
import com.commerce.saleday.domain.stock.model.ItemStock;
import com.commerce.saleday.domain.stock.model.QItemStock;
import com.commerce.saleday.domain.stock.repository.ItemStockRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemStockRepositoryImpl implements ItemStockRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public ItemStock findItemStockByItemCode(String itemCode) {
    QItemStock itemStock = QItemStock.itemStock;
    QItem item = QItem.item;

    return Optional.ofNullable(
            jpaQueryFactory
                .selectFrom(itemStock)
                .join(itemStock.item, item)
                .fetchJoin()
                .where(item.code.eq(itemCode))
                .fetchOne()
        )
        .orElseThrow(() -> new EntityNotFoundException("상품 정보가 없습니다."));
  }
}
