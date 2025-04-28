package com.commerce.saleday.api.domain.stock.port.model;

//common 모듈로 분리, MSA에서는 nexus나 별도 jar로 분리
public record DecreaseStockEvent(
    String itemCode,
    long quantity
) {}
