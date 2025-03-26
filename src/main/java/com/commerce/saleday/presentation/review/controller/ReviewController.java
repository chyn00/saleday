package com.commerce.saleday.presentation.review.controller;

import com.commerce.saleday.application.service.review.ReviewService;
import com.commerce.saleday.presentation.review.model.ReviewRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //item 정보를 사용자에게 노출
    @PostMapping("/review")
    public String saveReviewForItem(@RequestBody ReviewRequestDto dto){

//        return reviewService.saveReview("1234", dto.toEntity());
        return "0";
    }
}
