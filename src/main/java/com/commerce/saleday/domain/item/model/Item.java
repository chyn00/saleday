package com.commerce.saleday.domain.item.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)// 하이버네이트 Proxy에서 사용하도록 단계 조정
@AllArgsConstructor(access = AccessLevel.PRIVATE)// 외부에서 사용안할거지만, Lombok기본 Builder 생성에 필요
public class Item {
    @Id @GeneratedValue
    private Long id;// 고유번호가 아닌, +1로 생성되는 id
    private String code;//상품코드
    private String name;// 이름
    private String content;// 내용
}