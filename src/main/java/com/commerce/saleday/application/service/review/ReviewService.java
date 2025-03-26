package com.commerce.saleday.application.service.review;

import com.commerce.saleday.application.service.item.ItemService;
import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.review.model.Review;
import com.commerce.saleday.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ItemService itemService;

    //리뷰를 저장한다.
    @Transactional
    public Long saveReview(String itemCode, Review review){
        Item item = itemService.getItem(itemCode);
        review.mapTo(item);//item을 리뷰에 매핑
        return reviewRepository.createItemReview(review).getId();
    }
}
