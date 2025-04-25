package com.commerce.saleday.api.application.service.stock;

import com.commerce.saleday.api.domain.stock.model.ItemStock;
import com.commerce.saleday.api.domain.stock.port.ItemStockPort;
import com.commerce.saleday.api.domain.stock.repository.ItemStockRepository;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemStockService {

  private final ItemStockRepository itemStockRepository;
  private final ItemStockPort itemStockPort;

  //동시성 제어 측면에서 그냥 사용하면 위험, 멀티 인스턴스 환경에서 분산락 필요
  //관리자 입장에서의 상품 "개수" 저장 로직 - 재고관리가 되게 되면 단순 재고수량 외에 관리 포인트가 늘어난다.
  @Transactional
  public Long setUpItemStock(String itemCode, ItemStock itemStock) {

    String remaining = itemStockPort.getItemStock(itemCode);
    if (StringUtil.isNotEmpty(remaining)) {
      //남은 재고 수량이 있을 경우
      long remainingStock = Long.parseLong(remaining);
      itemStockPort.setInitialItemStock(itemCode, itemStock.getQuantity() + remainingStock);
    } else {
      //남은 재고 수량이 없을 경우
      itemStockPort.setInitialItemStock(itemCode, itemStock.getQuantity());
    }

    return itemStockRepository.save(itemStock).getId();
  }

  /**
   * 재고 수량이 감소된다. 레디스를 사용해도 select을 먼저할 경우, 동시성 문제에서 자유롭지 못하다. 싱글쓰레드 atomic 기반의 decrement를 사용하여 원자성을
   * 확보하고 정해진 수량 아래일 경우 로직을 진행하지 않도록 한다. 레디스 decr 및 진행 가능 여부 확인 함수 레디스에서 확인 가능 하면 order 처리하나, 고 트래픽의
   * 경우 동시성 부하가 심할 수 있어서 카프카 벌크(배치) 처리로 나눠서 부하 적용 필요 특성상 decrement, count 가 같이 일어나야하는 거라서 하나의 method
   * 로 통일
   */
  public Long decrementAndCountItemStock(String itemCode) {

    return itemStockPort.decrementItemStock(itemCode);
  }

  //재고 수량 증가
  public Long incrementAndCountItemStock(String itemCode) {

    return itemStockPort.incrementItemStock(itemCode);
  }
}
