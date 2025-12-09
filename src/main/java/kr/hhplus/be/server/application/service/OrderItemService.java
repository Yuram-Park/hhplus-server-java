package kr.hhplus.be.server.application.service;

import kr.hhplus.be.server.datasource.OrderItemRepositoryImpl;
import kr.hhplus.be.server.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepositoryImpl orderItemRepository;

    public List<OrderItem> saveAllOrderItems(List<OrderItem> orderItems) {
        return orderItemRepository.saveAllOrderItems(orderItems);
    }

}
