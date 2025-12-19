package kr.hhplus.be.server.application.event;

import kr.hhplus.be.server.application.interfaces.SalesProductRepository;
import kr.hhplus.be.server.dto.OrderItemEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final SalesProductRepository salesProductRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void complete(OrderEvent orderEvent) {
        Map<String,Integer> salesData = orderEvent.getOrderItemEventDtos().stream()
                .collect(Collectors.groupingBy(
                        orderItemEventDto -> String.valueOf(orderItemEventDto.getProductId()),
                        Collectors.summingInt(OrderItemEventDto::getOrderItemQuantity)
                ));
        salesProductRepository.addSalesQuantity(salesData);
    }

}
