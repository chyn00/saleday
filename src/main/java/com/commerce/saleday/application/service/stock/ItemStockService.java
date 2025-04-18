package com.commerce.saleday.application.service.stock;

import com.commerce.saleday.domain.stock.model.ItemStock;
import com.commerce.saleday.domain.stock.repository.ItemStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemStockService {

  private final ItemStockRepository itemStockRepository;

  //관리자 입장에서의 상품 "개수" 저장 로직 - 재고관리가 되게 되면 단순 재고수량 외에 관리 포인트가 늘어난다.
  @Transactional
  public Long saveItemStock(ItemStock itemStock) {

    return itemStockRepository.save(itemStock).getId();
  }
}
