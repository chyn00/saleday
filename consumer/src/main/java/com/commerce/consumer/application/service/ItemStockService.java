package com.commerce.consumer.application.service;

import com.commerce.consumer.infra.database.repository.ItemStockConsumerRepositoryImpl;
import com.commerce.saleday.domain.stock.model.ItemStock;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemStockService {

  private final ItemStockConsumerRepositoryImpl itemStockConsumerRepository;

  //dirty check update 활용하여 save 없이 저장.
  //성능 향상을 위해 batch 로 개발
  @Transactional
  public void decreaseStock(List<DecreaseStockEvent> decreaseStockEvents) {

    //map으로 분리(매핑시 재사용 위해서)
    Map<String, Long> stockRequestQuantityMap = decreaseStockEvents.stream()
        .collect(Collectors.toMap(
            DecreaseStockEvent::getItemCode,
            DecreaseStockEvent::getQuantity
        ));


    //itemCode만 분리
    List<String> itemCodes = decreaseStockEvents
        .stream()
        .map(DecreaseStockEvent::getItemCode)
        .toList();

    //Read : itemStocks list로 추출
    List<ItemStock> itemStocks = itemStockConsumerRepository.findItemStocksByItemCodes(itemCodes);

    int batchSize = 100;
    int count = 0;
    int itemStocksSize = itemStocks.size();

    //Process & Write :itemStocks 의 quantity 변경하여, 배치처리로 저장
    for(ItemStock itemStock : itemStocks){
      itemStock.decrease(stockRequestQuantityMap.get(itemStock.getItem().getCode()));
      count++;

      //카프카 listener 에서 1차캐싱영역에 gc가 일어나지 않을정도로 제어해준 뒤, flush를 활용하여 배치 처리
      //todo: 실시간 배치 쪽 처리하면서, 성능 튜닝과 GC처리 deep 하게 공부
      if(count % batchSize == 0 || count == itemStocksSize){
        itemStockConsumerRepository.flush();
      }
    }

  }

}
