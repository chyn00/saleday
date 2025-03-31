package com.commerce.saleday.presentation.review.model;

import com.commerce.saleday.domain.review.model.Review;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewRequestDto {

  @NotBlank(message = "상품코드는 필수입니다")
  String itemCode;//Item 코드(리뷰를 위한)

  @NotBlank(message = "유저 ID는 필수입니다")
  private String userId;

  @DecimalMin(value = "0.0", message = "점수는 0점 이상이어야 합니다")
  @DecimalMax(value = "5.0", message = "점수는 5점 이하이어야 합니다")
  private double score;

  @NotBlank(message = "리뷰 내용은 필수입니다")
  private String content;

  public Review toEntity() {
    return Review.builder()
        .userId(this.userId)
        .score(this.score)
        .content(this.content)
        .build();
  }
}
