package com.commerce.saleday.api.domain.order.repository;

import com.commerce.saleday.api.domain.order.model.Orders;
import java.util.Optional;

public interface OrderRepository {

  Optional<Orders> findOrderByCode(String orderCode);

  Orders createOrder(Orders order);
}
