package kr.hhplus.be.server.application.service;

import kr.hhplus.be.server.application.event.OrderEvent;
import kr.hhplus.be.server.datasource.OrderItemRepositoryImpl;
import kr.hhplus.be.server.domain.OrderItem;
import kr.hhplus.be.server.dto.OrderItemEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepositoryImpl orderItemRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<OrderItem> saveAllOrderItems(int orderId, List<OrderItem> orderItems) {
        List<OrderItem> result = orderItemRepository.saveAllOrderItems(orderItems);

        // Event: Redis에 주문한 productId, quantity 전달
        applicationEventPublisher.publishEvent(new OrderEvent(orderId, result.stream().map(OrderItemEventDto::from).toList()));

        return result;
    }

}
