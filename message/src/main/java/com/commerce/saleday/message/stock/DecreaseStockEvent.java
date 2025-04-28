package com.commerce.saleday.message.stock;

//common message 모듈로 분리(pub/sub 공용 사용 영역), MSA에서는 nexus나 별도 jar로 분리
public record DecreaseStockEvent(
    String itemCode,
    long quantity
) {

}

