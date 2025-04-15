package com.commerce.saleday.presentation.item.model;

import com.commerce.saleday.domain.review.model.Review;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemReviewResponseDto {

  private String userId;//유저 아이디

  private double score;// 점수

  private String content;// 내용

  public static List<ItemReviewResponseDto> toResponse(List<Review> reviews) {
    return reviews
        .stream()
        .map(review -> ItemReviewResponseDto
            .builder()
            .userId(review.getUserId())
            .score(review.getScore())
            .content(review.getContent())
            .build())
        .toList();
  }
}
