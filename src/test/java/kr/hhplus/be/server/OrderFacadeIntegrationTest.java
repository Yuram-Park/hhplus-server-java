package kr.hhplus.be.server;

import kr.hhplus.be.server.application.service.PointService;
import kr.hhplus.be.server.application.service.ProductService;
import kr.hhplus.be.server.datasource.ProductRepositoryImpl;
import kr.hhplus.be.server.datasource.UserRepositoryImpl;
import kr.hhplus.be.server.domain.Order;
import kr.hhplus.be.server.domain.Product;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.dto.ProductRequestDto;
import kr.hhplus.be.server.presentation.facade.OrderFacade;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.desktop.ScreenSleepEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderFacadeIntegrationTest extends IntegrationTestContext{

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private ProductRepositoryImpl productRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PointService pointService;

    @BeforeEach
    void setUp() {
        Product product = new Product("T01", "티셔츠", "하얀색 티셔츠", 1, 10_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        productRepository.updateByProductId(product);
        Product product2 = new Product("B01", "바지", "청바지", 100, 20_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        productRepository.updateByProductId(product2);
        User user = new User("abc123", "1234", null, null, 10_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);
        userRepository.updatePointById(user);
    }

    @Test
//    @Transactional(readOnly = false)
    @DisplayName("동시에 상품 재고 차감 시 하나만 성공한다.")
    void 상품_재고_동시성_테스트() throws InterruptedException {
        // given
        int orderQuantity = 1;

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        List<Order> finalOrderList = new ArrayList<>();

        // when
        for(int i = 0; i < 2; i++) {
            executor.submit(() -> {
                try {
                    Map<ProductRequestDto, Product> requestMap = new HashMap<>();
                    ProductRequestDto dto = new ProductRequestDto();
                    dto.setProductId("T01");
                    dto.setRequestQuantity(orderQuantity);
                    Product product = productRepository.findByProductId("T01").orElseThrow();
                    requestMap.put(dto, product);

                    finalOrderList.add(orderFacade.requestPayment("ID01", requestMap));
                } catch (Exception e) {
                    System.out.println("주문 실패: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // then
        assertThat(finalOrderList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 재고 차감 100건 분산락 적용 성공")
    void 상품재고차감_분산락_적용_동시성_테스트() throws InterruptedException {
        // given
        String productId = "B01";

        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for(int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    productService.reduceProduct(productId, 1);
                } finally {
                    latch.countDown();
                }

            });
        }
        latch.await();

        Product product = productService.getProductDetail(productId);

        assertThat(product.getProductInventory()).isEqualTo(0);
    }

    @Test
    @DisplayName("포인트 차감 100번 분산락 적용 성공")
    void 포인트_차감_분산락_적용_테스트() throws InterruptedException {
        // given
        String userId = "abc123";

        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for(int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    pointService.usePoint(userId, 100);
                } finally {
                    latch.countDown();
                }

            });
        }
        latch.await();

        User user = pointService.getUserPoint(userId);

        assertThat(user.getUserPoint()).isEqualTo(0);
    }
}
