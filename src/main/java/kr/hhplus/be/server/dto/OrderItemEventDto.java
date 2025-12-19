package kr.hhplus.be.server.dto;

import kr.hhplus.be.server.domain.OrderItem;

public class OrderItemEventDto {
    private String productId;
    private int orderItemQuantity;

    public static OrderItemEventDto from(OrderItem orderItem) {
        return new OrderItemEventDto(orderItem.getProductId(), orderItem.getOrderItemQuantity());
    }

    public OrderItemEventDto(String productId, int orderItemQuantity) {
        this.productId = productId;
        this.orderItemQuantity = orderItemQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setOrderItemQuantity(int orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }
}
