package com.commerce.saleday.item.service.review.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record CreateReviewCommand(
    @NotBlank(message = "상품코드는 필수입니다")
    String itemCode,
    @NotBlank(message = "유저 ID는 필수입니다")
    String userId,
    @DecimalMin(value = "0.0", message = "점수는 0점 이상이어야 합니다")
    @DecimalMax(value = "5.0", message = "점수는 5점 이하이어야 합니다")
    double score,
    @NotBlank(message = "리뷰 내용은 필수입니다")
    String content
) {

}
