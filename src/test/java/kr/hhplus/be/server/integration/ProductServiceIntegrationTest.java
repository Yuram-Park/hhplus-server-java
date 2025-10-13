package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.IntegrationTestContext;
import kr.hhplus.be.server.application.service.ProductService;
import kr.hhplus.be.server.datasource.ProductRepositoryImpl;
import kr.hhplus.be.server.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
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

    @BeforeEach
    void setUp() {
        Product product = new Product("T01", "티셔츠", "하얀색 티셔츠", 100, 100, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        productRepository.updateByProductId(product);
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
