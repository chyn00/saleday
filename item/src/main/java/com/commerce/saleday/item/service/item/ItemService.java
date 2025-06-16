package com.commerce.saleday.item.service.item;

import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.domain.item.repository.ItemRepository;
import com.commerce.saleday.item.domain.item.repository.ItemReviewRepository;
import com.commerce.saleday.item.domain.review.model.Review;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;
  private final ItemReviewRepository itemReviewRepository;

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

  //관리자 입장에서의 상품 저장 로직
  @Transactional
  public List<Item> saveAll(List<Item> items) {
    return itemRepository.saveAll(items);
  }

  //containing으로 복수조회
  public Page<Item> getItemsByCodeContaining(String code, Pageable pageable) {
    return itemRepository.findByCodeContains(code, pageable);
  }
}
