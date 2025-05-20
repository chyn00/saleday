package com.commerce.saleday.pay.domain.model;

import com.commerce.saleday.domain.common.BaseEntity;
import com.commerce.saleday.order.domain.order.model.Orders;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 하이버네이트 Proxy에서 사용하도록 단계 조정
public class Payment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)//연관관계의 주인이라 lazy 가능
  @JoinColumn(name = "orders_id", unique = true)//유니크는 사실 강제로 걸리는데, 명시용으로 코딩
  private Orders order;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status; // PENDING, SUCCESS, FAILED, CANCELLED

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentProvider provider; // KAKAO_PAY, TOSS, NICE 등

  @Column(nullable = false, unique = true)
  private String saleDayExternalSecretKey; // 우리 시스템이 PG에 전달하는 고유키 (idempotency key)

  private String pgTransactionId; // PG사에서 발급해주는 transaction id (ex. tid)

  private LocalDateTime approvedAt; // 승인 DateTime
}
