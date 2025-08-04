package kr.hhplus.be.server.service;

import kr.hhplus.be.server.IntegrationTestContext;
import kr.hhplus.be.server.application.service.OrderService;
import kr.hhplus.be.server.datasource.OrderRepositoryImpl;
import kr.hhplus.be.server.datasource.UserRepositoryImpl;
import kr.hhplus.be.server.domain.Order;
import kr.hhplus.be.server.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class OrderServiceIntegrationTest extends IntegrationTestContext {

    @Autowired
    private OrderRepositoryImpl orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUser() {
        userRepository.save(new User("ID01", "1111", null, null, 50_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
    }

    @Nested
    @DisplayName("상품 결제 요청 시")
    class OrderRequest {

        @Test
        @DisplayName("결제 성공 시 주문내역이 생성된다.")
        void 결제_성공() {
            // given
            String userId = "ID01";
            int originalPaymentAmount = 30_000;
            Integer couponId = null;
            int discountPaymentAmount = 0;

            User user = userRepository.findById(userId).orElseThrow();

            Order newOrder = new Order(null, user.getUserId(), originalPaymentAmount, couponId, discountPaymentAmount, originalPaymentAmount - discountPaymentAmount, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

            // when
            Order result = orderService.createOrder(newOrder);

            // then
            assertThat(result.getOrderId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("주문 조회 시")
    class GetOrder {

        @Test
        @DisplayName("주문 리스트를 조회 할 수 있다.")
        void 주문_리스트_조회_성공() {
            // given
            String userId = "ID01";
            orderService.createOrder(new Order(null, userId, 30_000, null, 0, 30_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            orderService.createOrder(new Order(null, userId, 50_000, 1, 5000, 45_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            orderService.createOrder(new Order(null, userId, 40_000, null, 0, 40_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

            // when
            List<Order> result = orderService.getOrderList(userId);

            // then
            assertThat(result).extracting("userId", "originalPaymentAmount", "couponId", "discountPaymentAmount", "finalPaymentAmount")
                    .containsExactlyInAnyOrder(
                            tuple(userId, 30_000, null, 0, 30_000),
                            tuple(userId, 50_000, 1, 5000, 45_000),
                            tuple(userId, 40_000, null, 0, 40_000)
                    );
        }

        @Test
        @DisplayName("주문 상세 내역을 조회 할 수 있다.")
        void 주문_상세_조회_성공() {
            // given
            String userId = "ID01";
            int originalPaymentAmount = 30_000;
            Integer couponId = null;
            int discountPaymentAmount = 0;
            int finalPaymentAmount = originalPaymentAmount - discountPaymentAmount;
            orderService.createOrder(new Order(null, userId, originalPaymentAmount, couponId, discountPaymentAmount, finalPaymentAmount, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            List<Order> userOrder = orderRepository.findByUserId(userId);

            // when
            int orderId = userOrder.get(0).getOrderId();
            Order result = orderService.getOrder(orderId).orElseThrow();

            // then
            assertThat(result).extracting("orderId", "userId", "originalPaymentAmount", "couponId", "discountPaymentAmount", "finalPaymentAmount")
                    .containsExactlyInAnyOrder(orderId, "ID01", originalPaymentAmount, couponId, discountPaymentAmount, finalPaymentAmount);
        }
    }
}
