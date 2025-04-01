package com.commerce.saleday.domain.order.repository;

import com.commerce.saleday.domain.order.model.Orders;
import java.util.Optional;

public interface OrderRepository {

  Optional<Orders> findOrderByCode(String orderCode);
}
