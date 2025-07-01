package com.commerce.saleday.api.controller.internal.pay.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ExternalPayRequest {

  @NotBlank(message = "userId는 필수입니다.")
  String userId;

  @NotBlank(message = "orderCode는 필수입니다.")
  String orderCode;
}
