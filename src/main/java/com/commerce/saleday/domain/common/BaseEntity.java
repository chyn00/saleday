package com.commerce.saleday.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass//필드 상속, 자식테이블에 컬럼추가
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;    // 생성일시

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String createdBy;           // 생성자

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;    // 수정일시

    @LastModifiedBy
    @Column(nullable = false)
    private String updatedBy;           // 수정자
}
