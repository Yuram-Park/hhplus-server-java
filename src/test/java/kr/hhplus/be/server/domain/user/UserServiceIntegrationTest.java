package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.IntegrationTestContext;
import kr.hhplus.be.server.application.service.UserService;
import kr.hhplus.be.server.domain.User;
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

@DisplayName("UserService 통합테스트")
public class UserServiceIntegrationTest extends IntegrationTestContext {

    @Autowired
    private UserService userService;

    private User userExample;

    @BeforeEach
    void setUp() {
        User user = new User("user1", "1111", null, null, 5000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);
        userExample = userService.registerUser(user);
    }

    @Nested
    @DisplayName("포인트 동시성 테스트")
    class UserLock {

        @Test
        @DisplayName("포인트 충전 동시 요청 시 최초 한번만 정상적으로 충전된다.")
        void 포인트_충전_동시성_성공() throws InterruptedException {
            // given
            int chargePoint = 1000;
            int numberOfThreads = 2;
            String userId = userExample.getUserId();

            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            AtomicInteger success = new AtomicInteger(0);
            AtomicInteger fail = new AtomicInteger(0);

            // when
            for(int i = 0; i < numberOfThreads; i++) {
                executorService.submit(() -> {
                    try {
                        userService.chargePoint(userId, chargePoint);
                        success.incrementAndGet();
                    } catch(Exception e) {
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
            User result = userService.getUserPoint(userId);

            assertThat(result.getUserPoint()).isEqualTo(userExample.getUserPoint() + chargePoint);
            assertThat(success.get()).isEqualTo(1);
            assertThat(fail.get()).isEqualTo(1);
        }

        @Test
        @DisplayName("포인트 사용 동시 요청 시 최초 한번만 정상적으로 사용된다.")
        void 포인트_사용_동시성_성공() throws InterruptedException {
            // given
            String userId = userExample.getUserId();
            int usePoint = 1000;
            int numberOfThreads = 2;

            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch latch = new CountDownLatch(numberOfThreads);
            AtomicInteger success = new AtomicInteger(0);
            AtomicInteger fail = new AtomicInteger(0);

            // when
            for(int i = 0; i < numberOfThreads; i++) {
                executorService.submit(() -> {
                    try {
                        userService.usePoint(userId, usePoint);
                        success.incrementAndGet();
                    } catch (Exception e) {
                        fail.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

            // then
            User result = userService.getUserPoint(userId);

            assertThat(result.getUserPoint()).isEqualTo(userExample.getUserPoint() - usePoint);
            assertThat(success.get()).isEqualTo(1);
            assertThat(fail.get()).isEqualTo(1);
        }
    }
}
