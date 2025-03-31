package com.commerce.saleday.domain.order.model;

import com.commerce.saleday.domain.common.BaseEntity;
import com.commerce.saleday.domain.item.model.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 하이버네이트 Proxy에서 사용하도록 단계 조정
@AllArgsConstructor(access = AccessLevel.PRIVATE)// 외부에서 사용안할거지만, Lombok기본 Builder 생성에 필요
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// 고유번호가 아닌, +1로 생성되는 id

    @Column(nullable = false)
    private String code;//주문 코드

    @Column(nullable = false)
    private String orderDate;//주문한 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item; //아이템
}
