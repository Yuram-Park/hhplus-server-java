package kr.hhplus.be.server.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import kr.hhplus.be.server.application.service.OrderService;
import kr.hhplus.be.server.datasource.OrderRepositoryImpl;
import kr.hhplus.be.server.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 테스트")
public class OrderServiceTest {

    @Mock
    private OrderRepositoryImpl orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Nested
    @DisplayName("상품 결제 요청 시")
    class OrderRequest {

        @Test
        @DisplayName("결제 성공 시 주문내역이 생성된다.")
        void 결제_성공() {
            // given
            Integer orderId = 1; // auto_increment인 경우 어떻게 테스트?
            String userId = "ID01";
            int originalPaymentAmount = 30_000;
            Integer couponId = null;
            int discountPaymentAmount = 0;

            Order newOrder = new Order(orderId, userId, originalPaymentAmount, couponId, discountPaymentAmount, originalPaymentAmount - discountPaymentAmount, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            when(orderRepository.createOrder(any())).thenReturn(newOrder);

            // when
            Order result = orderService.createOrder(newOrder);

            // then
            assertThat(result.getOrderId()).isEqualTo(orderId);
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

            List<Order> orderList = new ArrayList<>();
            orderList.add(new Order(1, userId, 30_000, null, 0, 30_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            orderList.add(new Order(2, userId, 50_000, 1, 5000, 45_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            orderList.add(new Order(3, userId, 40_000, null, 0, 40_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

            when(orderRepository.findByUserId(anyString())).thenReturn(orderList);

            // when
            List<Order> result = orderService.getOrderList(userId);

            // then
            assertThat(result).extracting("orderId", "userId", "originalPaymentAmount", "couponId", "discountPaymentAmount", "finalPaymentAmount")
                    .containsExactlyInAnyOrder(
                            tuple(1, userId, 30_000, null, 0, 30_000),
                            tuple(2, userId, 50_000, 1, 5000, 45_000),
                            tuple(3, userId, 40_000, null, 0, 40_000)
                    );
        }

        @Test
        @DisplayName("주문 상세 내역을 조회 할 수 있다.")
        void 주문_상세_조회_성공() {
            // given
            int orderId = 1;

            Order order = new Order(orderId, "ID01", 30_000, null, 0, 30_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            when(orderRepository.findByOrderId(anyInt())).thenReturn(order);

            // when
            Order result = orderService.getOrder(orderId);

            // then
            assertThat(result).extracting("orderId", "userId", "originalPaymentAmount", "couponId", "discountPaymentAmount", "finalPaymentAmount")
                    .containsExactlyInAnyOrder(orderId, "ID01", 30_000, null, 0, 30_000);
        }
    }
}
