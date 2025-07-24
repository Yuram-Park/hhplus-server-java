package kr.hhplus.be.server.application.service;

import kr.hhplus.be.server.datasource.OrderRepositoryImpl;
import kr.hhplus.be.server.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepositoryImpl orderRepository;

    /**
     * 주문 내역 생성
     * @param order
     * @return
     */
    public Order createOrder(Order order) {
        return orderRepository.createOrder(order);
    }

    /**
     * 아이디 별 주문 내역 리스트 조회
     * @param userId
     * @return
     */
    public List<Order> getOrderList(String userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * 단일 주문 내역 조회
     * @param orderId
     * @return
     */
    public Order getOrder(int orderId) {
        return orderRepository.findByOrderId(orderId);
    }
}
