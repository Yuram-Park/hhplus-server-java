package kr.hhplus.be.server.application.jpa;

import io.lettuce.core.dynamic.annotation.Param;
import kr.hhplus.be.server.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemJapRepository extends JpaRepository<OrderItem, Integer> {

    @Query("SELECT o FROM OrderItem o WHERE o.orderId = :orderId")
    List<OrderItem> findOrderItemsByOrderId(@Param("orderId") int orderId);
}
