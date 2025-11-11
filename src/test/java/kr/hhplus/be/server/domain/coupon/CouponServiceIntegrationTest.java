package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.IntegrationTestContext;
import kr.hhplus.be.server.application.service.CouponService;
import kr.hhplus.be.server.datasource.CouponRepositoryImpl;
import kr.hhplus.be.server.domain.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CouponService 통합테스트")
public class CouponServiceIntegrationTest extends IntegrationTestContext {

    @Autowired
    private CouponService couponService;

    private Coupon couponExample;

    @BeforeEach
    void setUp() {
        couponExample = couponService.createCoupon(new Coupon("A", 10, 100));
    }

    @Nested
    @DisplayName("동시성 테스트")
    class LockCoupon {

        @Test
        @DisplayName("100번의 쿠폰 발급 요청 시 기존 재고 차감이 정상적으로 이루어진다.")
        void 재고_차감_동시성_성공() throws InterruptedException {
            // given
            int numberOfThreads = 100;
            int reduceNum = 1;
            String couponType = couponExample.getCouponType();

            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            // when
            executorService.submit(() -> {
                try {
                    couponService.reduceCoupon(couponType, reduceNum);
                } finally {
                    latch.countDown();
                }
            });

            latch.await();
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);

            // then
            Coupon result = couponService.getCouponInfo(couponType);
            assertThat(result.getCouponInventory()).isEqualTo(couponExample.getCouponInventory() - numberOfThreads * reduceNum);
        }
    }
}
