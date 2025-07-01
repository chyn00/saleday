package com.commerce.saleday.item.service.item;

import com.commerce.saleday.common.exception.ExceptionCode;
import com.commerce.saleday.common.exception.SaleDayException;
import com.commerce.saleday.item.domain.item.ItemCreateCommand;
import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.domain.item.repository.ItemRepository;
import com.commerce.saleday.item.domain.item.repository.ItemReviewRepository;
import com.commerce.saleday.item.domain.review.model.Review;
import com.commerce.saleday.item.infra.redis.RedisItemIdGenerator;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

  public static final int BATCH_SIZE = 1000;
  private final ItemRepository itemRepository;
  private final ItemReviewRepository itemReviewRepository;
  private final RedisItemIdGenerator redisItemIdGenerator;

  public Item getItem(String itemCode) {
    return itemRepository.findItemByCode(itemCode)
        .orElseThrow(() -> new EntityNotFoundException("상품 정보가 없습니다."));
  }

  //eqauls 로 복수조회
  public List<Item> getItemsByItemCode(List<String> itemCodeList) {
    return itemRepository.findItemsByCode(itemCodeList);
  }

  //Item을 기준으로 Review를 조회하기 때문에, Item package 하위에 위치
  public List<Review> getItemReviews(String itemCode) {
    return itemReviewRepository.findReviewsWithItem(itemCode);
  }

  //관리자 입장에서의 상품 저장 로직
  @Transactional
  public Long save(Item item) {

    return itemRepository.save(item).getId();
  }

  //선행 작업,
  //관리자 입장에서 bulk 상품 저장 로직
  @Transactional
  public boolean saveAll(List<ItemCreateCommand> items) {
    long itemId = redisItemIdGenerator.generateBatchAndGetStartId(BATCH_SIZE);
    List<Item> itemsPartition = new ArrayList<>(BATCH_SIZE);

    for(ItemCreateCommand itemCreateCommand : items){
      Item item = Item.create
          (itemId
              , itemCreateCommand.getCode()
              , itemCreateCommand.getName()
              , itemCreateCommand.getContent()
              , itemCreateCommand.getPrice()
              , itemCreateCommand.getReviews());

      itemId++;// Idcount 반드시 필요

      itemsPartition.add(item);// list에 저장

      //메모리를 효율적으로 사용하기 위해 1000마다 list reset
      if (itemsPartition.size() % BATCH_SIZE == 0) {
        itemRepository.persistAll(itemsPartition);
        itemsPartition.clear();// 명시적으로 객체 리셋
      }
    }

    // 1000으로 떨어지지 않은 경우 저장
    if (!itemsPartition.isEmpty()) {
      itemRepository.persistAll(itemsPartition);
    }

    return true;
  }

  //containing으로 복수조회
  public Page<Item> getItemsByCodeContaining(String code, Pageable pageable) {
    if (code == null || code.isBlank()) {
      throw new SaleDayException(ExceptionCode.NO_SUCH_DATA);
    }
    return itemRepository.findByCodeContains(code, pageable);
  }
}
