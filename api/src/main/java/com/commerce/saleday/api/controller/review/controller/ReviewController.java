package com.commerce.saleday.api.controller.review.controller;

import com.commerce.saleday.api.controller.review.model.ReviewRequestDto;
import com.commerce.saleday.item.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  //Review 생성
  @PostMapping("/review")
  public Long saveReviewForItem(@RequestBody ReviewRequestDto dto) {

    return reviewService.saveReview(dto.toCommand());
  }
}
