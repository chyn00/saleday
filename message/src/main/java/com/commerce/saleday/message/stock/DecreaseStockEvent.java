package com.commerce.saleday.message.stock;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

//common message 모듈로 분리(pub/sub 공용 사용 영역), MSA에서는 nexus나 별도 jar로 분리
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DecreaseStockEvent {

  private String itemCode;
  private long quantity;
  private String eventId;

  public void initEventId(String eventId) {
    this.eventId = eventId;
  }

  public static DecreaseStockEvent toEventMessage(String itemCode, long quantity) {
    return new DecreaseStockEvent(itemCode, quantity, null); // 초기 eventId는 null로 처리
  }
}

