package com.commerce.saleday.infra.database.repository.order;

import com.commerce.saleday.domain.order.model.Orders;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

//Jpa Data Repository 인터페이스
public interface OrderJpaRepository extends JpaRepository<Orders, Long> {

  Optional<Orders> findOrderByCode(String orderCode);
}
