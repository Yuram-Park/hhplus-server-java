package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.OrderRepository;
import kr.hhplus.be.server.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @Override
    public Order createOrder(Order order) {
        return null;
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return List.of();
    }

    @Override
    public Order findByOrderId(int orderId) {
        return null;
    }
}
