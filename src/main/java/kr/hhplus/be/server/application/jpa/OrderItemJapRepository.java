package kr.hhplus.be.server.application.jpa;

import kr.hhplus.be.server.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemJapRepository extends JpaRepository<OrderItem, Integer> {

}
