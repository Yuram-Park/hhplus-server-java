package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.IntegrationTestContext;
import kr.hhplus.be.server.application.service.UserCouponService;
import kr.hhplus.be.server.datasource.UserCouponRepositoryImpl;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.domain.UserCoupon;
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
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserCouponService 통합 테스트")
public class UserServiceIntegrationTest extends IntegrationTestContext {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private UserCouponRepositoryImpl userCouponRepository;

    private User user;
    private UserCoupon userCoupon;

    @BeforeEach
    void setUp() {
        user = new User("userId", "1111", null, null, 50000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null); // 더 쉽게 생성하는 법?
        userCoupon = userCouponService.createUserCoupon(user.getUserId(), 'A');
    }

    @Nested
    @DisplayName("동시성 테스트")
    class LockUserCoupon {

        @Test
        @DisplayName("100번의 쿠폰 사용 요청 시 쿠폰 사용여부가 정상적으로 이루어진다.")
        void 사용자_쿠폰_사용_동시성_정상() throws InterruptedException {
            // given
            int payment = 55000;
            int numberOfThreads = 100;
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            AtomicInteger success = new AtomicInteger(0);
            AtomicInteger fail = new AtomicInteger(0);

            // when
            for(int i=0; i<numberOfThreads; i++) {
                executorService.submit(() -> {
                    try {
                        userCouponService.useUserCoupon(userCoupon.getCouponId(), payment);
                        success.incrementAndGet();
                    } catch (Exception e) {
                        fail.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);

            // then
            UserCoupon result = userCouponService.getUserCouponInfo(userCoupon.getCouponId());
            assertThat(result.isUsed()).isEqualTo(true);
            assertThat(success.get()).isEqualTo(1);
            assertThat(fail.get()).isEqualTo(numberOfThreads - 1);
        }
    }
}
