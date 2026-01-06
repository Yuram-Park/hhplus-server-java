package kr.hhplus.be.server.application.event;

import kr.hhplus.be.server.dto.OrderItemEventDto;
import lombok.*;

import java.util.List;

/**
 * Daily Product Count Update 실패 시 발행하는 보상트랜잭션 Event 객체
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailySalesUpdateFailedEvent {
    private int orderId;
    private List<OrderItemEventDto> orderItemEventDtos;
}
