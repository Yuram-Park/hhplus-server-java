package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.IntegrationTestContext;
import kr.hhplus.be.server.application.service.ProductService;
import kr.hhplus.be.server.datasource.OrderItemRepositoryImpl;
import kr.hhplus.be.server.datasource.ProductRepositoryImpl;
import kr.hhplus.be.server.domain.OrderItem;
import kr.hhplus.be.server.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductService 통합테스트")
public class ProductServiceIntegrationTest extends IntegrationTestContext {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepositoryImpl productRepository;

    @Autowired
    private OrderItemRepositoryImpl orderItemRepository;

    @BeforeEach
    void setUp() {
        Product product1 = new Product("T01", "티셔츠", "하얀색 티셔츠", 100, 10000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        Product product2 = new Product("S01", "스커트", "파란색 스커트", 100, 10000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        Product product3 = new Product("P01", "팬츠", "청바지", 100, 10000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        productRepository.updateByProductId(product1);
        productRepository.updateByProductId(product2);
        productRepository.updateByProductId(product3);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem(null, 1, "T01", 10, 100000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        orderItems.add(new OrderItem(null, 1, "S01", 20, 100000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        orderItems.add(new OrderItem(null, 2, "T01", 5, 50000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        orderItems.add(new OrderItem(null, 2, "P01", 1, 10000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        orderItemRepository.saveAllOrderItems(orderItems);
    }

    @Nested
    @DisplayName("상품 조회 시")
    class GetProduct {

        @Test
        @DisplayName("당일 인기상품을 조회 할 수 있다.")
        void 당일_인기상품_조회_성공() {
            // given
            LocalDate startDate = LocalDate.now();

            List<Product> popularList = new ArrayList<>();
            popularList.add(new Product("S01", "스커트", "파란색 스커트", 100, 10000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            popularList.add(new Product("T01", "티셔츠", "하얀색 티셔츠", 100, 10000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            popularList.add(new Product("P01", "팬츠", "청바지", 100, 10000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            // when
            List<Product> resultList = productService.findPopularProducts(startDate);

            // then
            for(int i=0; i<resultList.size(); i++) {
                assertThat(resultList.get(i).getProductId()).isEqualTo(popularList.get(i).getProductId());
            }
        }
    }

    @Nested
    @DisplayName("동시성 테스트")
    class LockProduct {

        @Test
        @DisplayName("상품 주문 100번 요청 시 재고 차감이 정상적으로 성공한다.")
        void 재고_차감_동시성_성공() throws InterruptedException {
            // given
            String productId = "T01";

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
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);

            Product product = productService.getProductDetail(productId);

            assertThat(product.getProductInventory()).isEqualTo(0);
        }
    }
}
