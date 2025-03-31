package com.commerce.saleday.domain.order.model;

import com.commerce.saleday.domain.common.BaseEntity;
import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.review.model.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    //연관관계 지워질때도 연관되도록 구현
    @OneToMany(mappedBy = "orderItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
