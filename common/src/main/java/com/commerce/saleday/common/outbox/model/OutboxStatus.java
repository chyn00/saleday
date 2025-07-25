package com.commerce.saleday.common.outbox.model;

/**
 * 아웃박스 메시지의 상태값.
 */
public enum OutboxStatus {
    INIT,     // 메시지 발행 초기 상태
    PENDING,  // 메시지 발행 후 처리 중 상태
    SUCCESS,  // 메시지 발행 완료
    FAILED    // 메시지 발행 실패
}
