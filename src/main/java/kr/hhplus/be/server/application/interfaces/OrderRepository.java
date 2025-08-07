package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order createOrder(Order order);
    List<Order> findByUserId(String userId);
    Optional<Order> findByOrderId(int orderId);
}
