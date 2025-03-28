package com.commerce.saleday.domain.review.model;

import com.commerce.saleday.domain.common.BaseEntity;
import com.commerce.saleday.domain.item.model.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 하이버네이트 Proxy에서 사용하도록 단계 조정
@AllArgsConstructor(access = AccessLevel.PRIVATE)// 외부에서 사용안할거지만, Lombok기본 Builder 생성에 필요
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// 고유번호가 아닌, +1로 생성되는 id

    @Column(nullable = false)
    private String userId;//유저 아이디

    @Column(nullable = false)
    private double score;// 점수

    @Column(nullable = false)
    private String content;// 내용

    // 연관관계의 주인(Review라는 객체의 관리 주체성이 여기에 있기 때문에, 연관관계의 주인 표현)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)//Item없이 존재 불가 null
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    // Item Mapping
    public void mapTo(Item item) {
        this.item = item;
    }
}
