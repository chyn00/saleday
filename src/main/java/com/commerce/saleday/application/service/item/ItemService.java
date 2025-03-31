package com.commerce.saleday.application.service.item;

import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.item.repository.ItemRepository;
import com.commerce.saleday.domain.item.repository.ItemReviewRepository;
import com.commerce.saleday.domain.review.model.Review;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;
  private final ItemReviewRepository itemReviewRepository;

  public Item getItem(String itemCode) {
    return itemRepository.findItemByCode(itemCode)
        .orElseThrow(() -> new EntityNotFoundException("상품 정보가 없습니다."));
  }

  //Item을 기준으로 Review를 조회하기 때문에, Item package 하위에 위치
  public List<Review> getItemReviews(String itemCode) {
    return itemReviewRepository.findReviewsWithItem(itemCode);
  }
}
