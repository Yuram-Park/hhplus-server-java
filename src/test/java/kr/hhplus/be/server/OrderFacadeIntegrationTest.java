package kr.hhplus.be.server;

import kr.hhplus.be.server.datasource.ProductRepositoryImpl;
import kr.hhplus.be.server.domain.Order;
import kr.hhplus.be.server.domain.Product;
import kr.hhplus.be.server.dto.ProductRequestDto;
import kr.hhplus.be.server.presentation.facade.OrderFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @BeforeEach
    void setUp() {
        Product product = new Product("T01", "티셔츠", "하얀색 티셔츠", 1, 10_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        productRepository.updateByProductId(product);
    }

    @Test
    @Transactional(readOnly = false)
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

}
