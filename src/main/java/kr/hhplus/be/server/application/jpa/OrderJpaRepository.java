package kr.hhplus.be.server.application.jpa;

import kr.hhplus.be.server.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Integer> {

    Order save(Order order);

    List<Order> findAllByUserId(String user_Id);

    Order findById(int order_Id);
}
