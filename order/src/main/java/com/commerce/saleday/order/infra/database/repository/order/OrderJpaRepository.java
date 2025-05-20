package com.commerce.saleday.order.infra.database.repository.order;

import com.commerce.saleday.order.domain.order.model.Orders;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

//Jpa Data Repository 인터페이스
public interface OrderJpaRepository extends JpaRepository<Orders, Long> {

  @EntityGraph(attributePaths = {"orderItems", "orderItems.item"})//객체 내 연관관계대로, join 생성
  Optional<Orders> findOrderByCode(String orderCode);
}
