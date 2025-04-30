package com.commerce.saleday.domain.payment.model;

import lombok.Getter;

@Getter
public enum PaymentStatus {
  PENDING("결제 대기"),
  SUCCESS("결제 성공"),
  FAILED("결제 실패"),
  CANCELLED("결제 취소");

  private final String description;

  PaymentStatus(String description) {
    this.description = description;
  }

}
