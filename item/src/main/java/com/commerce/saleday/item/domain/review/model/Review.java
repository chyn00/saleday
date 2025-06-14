package com.commerce.saleday.item.domain.review.model;

import com.commerce.saleday.common.model.BaseEntity;
import com.commerce.saleday.item.domain.item.model.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 하이버네이트 Proxy에서 사용하도록 단계 조정
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;// +1로 생성되는 id(고유번호)

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

  @Builder(access = AccessLevel.PRIVATE)
  private Review(String userId, double score, String content, Item item) {
    this.userId = userId;
    this.score = score;
    this.content = content;
    this.item = item;
  }

  public static Review create(String userId, double score, String content, Item item){
    Review review = Review
        .builder()
        .userId(userId)
        .score(score)
        .content(content)
        .item(item)
        .build();
    item.getReviews().add(review);//양방향 매핑
    return review;
  }

  public void mapTo(Item item) {
    this.item = item;
  }
}
