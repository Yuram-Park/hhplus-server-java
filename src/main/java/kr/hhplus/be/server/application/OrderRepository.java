package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.Order;

import java.util.List;

public interface OrderRepository {

    public Order createOrder(Order order);
    public List<Order> findByUserId(String userId);
    public Order findByOrderId(int orderId);
}
