package kr.hhplus.be.server.application.event;

import kr.hhplus.be.server.dto.OrderItemEventDto;

import java.util.List;

public class OrderEvent {
    private int orderId;
    private List<OrderItemEventDto> orderItemEventDtos;

    public OrderEvent(int orderId, List<OrderItemEventDto> orderItemEventDtos) {
        this.orderId = orderId;
        this.orderItemEventDtos = orderItemEventDtos;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public List<OrderItemEventDto> getOrderItemEventDtos() {
        return orderItemEventDtos;
    }

    public void setOrderItemEventDtos(List<OrderItemEventDto> orderItemEventDtos) {
        this.orderItemEventDtos = orderItemEventDtos;
    }
}
