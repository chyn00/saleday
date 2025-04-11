package com.commerce.saleday.presentation.order.model.bulk;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BulkOrderRequestDto {

  @NotBlank(message = "유저 ID는 필수입니다.")
  private String userId;

  //다건 아이템 수량
  @Valid//내부 항목까지 확인
  @NotEmpty//단순 리스트의 빈값 확인
  private List<OrderItemRequest> orderItemRequestList;
}
