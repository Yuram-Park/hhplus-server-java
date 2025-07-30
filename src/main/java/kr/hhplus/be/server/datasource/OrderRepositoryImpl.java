package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.OrderRepository;
import kr.hhplus.be.server.application.jpa.OrderJpaRepository;
import kr.hhplus.be.server.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order createOrder(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return orderJpaRepository.findAllByUserId(userId);
    }

    @Override
    public Order findByOrderId(int orderId) {
        return orderJpaRepository.findById(orderId);
    }
}
