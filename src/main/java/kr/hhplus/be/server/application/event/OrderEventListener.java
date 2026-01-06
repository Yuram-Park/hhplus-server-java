package kr.hhplus.be.server.application.event;

import kr.hhplus.be.server.application.interfaces.DailySalesProductRepository;
import kr.hhplus.be.server.dto.OrderItemEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final DailySalesProductRepository dailySalesProductRepository;

    /**
     * Daily Product 판매 수량 누적 이벤트
     * @param orderEvent
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void dailySalesProductCount(OrderEvent orderEvent) {

        try {
            Map<String, Integer> salesData = orderEvent.getOrderItemEventDtos().stream()
                    .collect(Collectors.groupingBy(
                            orderItemEventDto -> String.valueOf(orderItemEventDto.getProductId()),
                            Collectors.summingInt(OrderItemEventDto::getOrderItemQuantity)
                    ));
            dailySalesProductRepository.addSalesQuantity(salesData);
        } catch (Exception e) {
            log.error("ERROR in DailySalesProduct - orderId : {}", orderEvent.getOrderId());
            e.printStackTrace();
        }
    }

}
