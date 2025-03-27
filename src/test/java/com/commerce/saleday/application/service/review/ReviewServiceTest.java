package com.commerce.saleday.application.service.review;

import com.commerce.saleday.domain.review.model.Review;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;

    @Test
    @Transactional
    void saveReview() {
        //given
        String itemCode = "1234";
        Review review = Review.builder()
                .userId("SYSTEM")
                .score(4.5)
                .content("아주 훌륭한 아이템입니다.")
                .build();

        //when
        Long id = reviewService.saveReview(itemCode, review);

        //then
        assertThat(id).isGreaterThan(0);
    }
}