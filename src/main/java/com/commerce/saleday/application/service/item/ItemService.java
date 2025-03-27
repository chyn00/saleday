package com.commerce.saleday.application.service.item;

import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.item.repository.ItemRepository;
import com.commerce.saleday.domain.item.repository.ItemReviewRepository;
import com.commerce.saleday.domain.review.model.Review;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemReviewRepository itemReviewRepository;

    public Item getItem(String itemCode){
        return itemRepository.findItemByCode(itemCode).orElseThrow(() -> new EntityNotFoundException("상품 정보가 없습니다."));
    }

    public List<Review> getItemReviews(String itemCode) {
        return itemReviewRepository.findReviewsWithItem(itemCode);
    }
}
