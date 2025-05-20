package com.commerce.saleday.order.domain.order.repository;

import com.commerce.saleday.order.domain.order.model.Orders;
import java.util.Optional;

public interface OrderRepository {

  Optional<Orders> findOrderByCode(String orderCode);

  Orders createOrder(Orders order);
}
