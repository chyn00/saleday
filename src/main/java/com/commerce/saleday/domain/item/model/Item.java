package com.commerce.saleday.domain.item.model;

import com.commerce.saleday.domain.common.BaseEntity;
import com.commerce.saleday.domain.review.model.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 하이버네이트 Proxy에서 사용하도록 단계 조정
public class Item extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;// 고유번호가 아닌, +1로 생성되는 id

  @Column(nullable = false)
  private String code;//상품코드

  @Column(nullable = false)
  private String name;// 이름

  private String content;// 내용

  private double price;// 가격

  //연관관계 지워질때도 연관되도록 구현
  @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> reviews;

  @Builder
  private Item(String code, String name, String content, double price, List<Review> reviews) {
    this.code = code;
    this.name = name;
    this.content = content;
    this.price = price;
    this.reviews = reviews;
  }
}
