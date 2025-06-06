package com.commerce.saleday.order.infra.database.repository.order;

import com.commerce.saleday.order.domain.order.model.Orders;
import com.commerce.saleday.order.domain.order.repository.OrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

  private final OrderJpaRepository orderJpaRepository;

  @Override
  public Optional<Orders> findOrderByCode(String orderCode) {
    return orderJpaRepository.findOrderByCode(orderCode);
  }

  @Override
  public Orders createOrder(Orders order) {
    return orderJpaRepository.save(order);
  }
}
