package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.OrderItemRepository;
import kr.hhplus.be.server.application.jpa.OrderItemJapRepository;
import kr.hhplus.be.server.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {
    private final OrderItemJapRepository orderItemJapRepository;

    @Override
    public List<OrderItem> saveAllOrderItems(List<OrderItem> orderItems) {
        return orderItemJapRepository.saveAll(orderItems);
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(int orderId) {
        return orderItemJapRepository.findOrderItemsByOrderId(orderId);
    }

    @Override
    public void deleteAllOrderItems(List<OrderItem> orderItems) {
        orderItemJapRepository.deleteAll(orderItems);
    }
}
