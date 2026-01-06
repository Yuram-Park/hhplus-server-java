package kr.hhplus.be.server.application.event;

import kr.hhplus.be.server.application.service.OrderItemService;
import kr.hhplus.be.server.domain.OrderItem;
import kr.hhplus.be.server.dto.OrderItemEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailySalesUpdateFailedEventListener {

    private final OrderItemService orderItemService;

    /**
     * Daily Product count update 실패 시 보상 트랜잭션 구현 : 해당 orderItem 삭제
     * TODO 사실 여기에 맞는 로직은 아님. 다른 로직을 찾아서 반영해보기!
     * @param failedEvent
     */
    @Async
    @EventListener
    public void afterFailed(DailySalesUpdateFailedEvent failedEvent) {
        int orderId = failedEvent.getOrderId();

        orderItemService.deleteAllOrderItems(orderId);
    }
}
