package com.commerce.saleday.domain.order.Entity;

import com.commerce.saleday.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 하이버네이트 Proxy에서 사용하도록 단계 조정
@AllArgsConstructor(access = AccessLevel.PRIVATE)// 외부에서 사용안할거지만, Lombok기본 Builder 생성에 필요
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// 고유번호가 아닌, +1로 생성되는 id

    @Column(nullable = false)
    private String code;//주문 코드
}
