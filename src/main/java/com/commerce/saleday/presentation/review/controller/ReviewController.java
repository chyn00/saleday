package com.commerce.saleday.presentation.review.controller;

import com.commerce.saleday.application.service.review.ReviewService;
import com.commerce.saleday.presentation.review.model.ReviewRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //Review 생성
    @PostMapping("/review")
    public Long saveReviewForItem(@RequestBody ReviewRequestDto dto){

        return reviewService.saveReview(dto.getItemCode(), dto.toEntity());
    }
}
