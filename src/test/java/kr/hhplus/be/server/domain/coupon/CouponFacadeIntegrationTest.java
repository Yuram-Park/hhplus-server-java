package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.IntegrationTestContext;
import kr.hhplus.be.server.application.service.CouponService;
import kr.hhplus.be.server.application.service.UserCouponService;
import kr.hhplus.be.server.application.service.UserService;
import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.domain.UserCoupon;
import kr.hhplus.be.server.presentation.facade.CouponFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CouponFacade 통합테스트")
public class CouponFacadeIntegrationTest extends IntegrationTestContext {

    @Autowired
    CouponFacade couponFacade;

    @Autowired
    CouponService couponService;

    @Autowired
    UserCouponService userCouponService;

    private Coupon couponExample;
    private User userExample;

    @BeforeEach
    void setUp() {
        couponExample = couponService.createCoupon(new Coupon("A", 10, 100));
        userExample = new User("userId", "1111", null, null, 50000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null); // 더 쉽게 생성하는 법?
    }

    @Nested
    @DisplayName("동시성 테스트")
    class LockCoupon {

        @Test
        @DisplayName("분산락 적용 동시성 테스트: 100번의 쿠폰 발급 요청 시 기존 재고 차감이 정상적으로 이루어진다.")
        void 재고_차감_분산락_동시성_성공() throws InterruptedException {
            // given
            int numberOfThreads = 100;
            int reduceNum = 1;
            String couponType = couponExample.getCouponType();

            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            // when
            for (int i = 0; i < numberOfThreads; i++) {
                executorService.submit(() -> {
                    try {
                        couponFacade.issuedCoupon(userExample, "A", 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);

            // then
            Coupon result = couponService.getCouponInfo(couponType);
            List<UserCoupon> userCouponList = userCouponService.getUserCouponList(userExample.getUserId());

            assertThat(result.getCouponInventory()).isEqualTo(couponExample.getCouponInventory() - numberOfThreads * reduceNum);
            assertThat(userCouponList.size()).isEqualTo(100);
        }
    }
}
