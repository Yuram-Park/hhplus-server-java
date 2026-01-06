package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.OrderItem;

import java.util.List;

public interface OrderItemRepository {
    List<OrderItem> saveAllOrderItems(List<OrderItem> orderItems);

    List<OrderItem> findOrderItemsByOrderId(int orderId);

    void deleteAllOrderItems(List<OrderItem> orderItems);
}
