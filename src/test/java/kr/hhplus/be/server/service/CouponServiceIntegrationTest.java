package kr.hhplus.be.server.service;

import kr.hhplus.be.server.IntegrationTestContext;
import kr.hhplus.be.server.application.service.CouponService;
import kr.hhplus.be.server.datasource.CouponRepositoryImpl;
import kr.hhplus.be.server.datasource.UserCouponRepositoryImpl;
import kr.hhplus.be.server.datasource.UserRepositoryImpl;
import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.domain.UserCoupon;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponServiceIntegrationTest extends IntegrationTestContext {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private CouponRepositoryImpl couponRepository;

    @Autowired
    private UserCouponRepositoryImpl userCouponRepository;

    @Autowired
    private CouponService couponService;

    @BeforeEach
    void setUsersAndCoupons() {
        userRepository.save(new User("ID01", "1111", null, null, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        userRepository.save(new User("ID02", "1111", null, null, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        userRepository.save(new User("ID03", "1111", null, null, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        userRepository.save(new User("ID04", "1111", null, null, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
        couponRepository.save(new Coupon('A', 30, 1));
        couponRepository.save(new Coupon('B', 20, 1));
        couponRepository.save(new Coupon('C', 10, 1));
    }

    @Nested
    @DisplayName("쿠폰 발급 시")
    class CouponIssue {

        @Test
        @DisplayName("선착순으로 user coupon이 발급된다.")
        void 선착순_쿠폰_발급_성공() {
            // given
            List<User> userList = new ArrayList<>();
            User user1 = userRepository.findById("ID01").orElseThrow();
            User user2 = userRepository.findById("ID02").orElseThrow();
            User user3 = userRepository.findById("ID03").orElseThrow();
            userList.add(user1);
            userList.add(user2);
            userList.add(user3);

            // when
            Map<String, UserCoupon> result = couponService.issueCoupon(userList);

            // then
            for(String userId : result.keySet()) {
                UserCoupon userCoupon = result.get(userId);

                assertThat(userCoupon.getUserId()).isEqualTo(userId);
            }

        }

        @Test
        @DisplayName("선착순에 들지 못하면 쿠폰 발급 실패")
        void 쿠폰_발급_실패() {
            // given
            List<User> userList = new ArrayList<>();
            User user1 = userRepository.findById("ID01").orElseThrow();
            User user2 = userRepository.findById("ID02").orElseThrow();
            User user3 = userRepository.findById("ID03").orElseThrow();
            User user4 = userRepository.findById("ID04").orElseThrow();
            userList.add(user1);
            userList.add(user2);
            userList.add(user3);
            userList.add(user4);

            // when
            Map<String, UserCoupon> result = couponService.issueCoupon(userList);

            // then
            int idx = 0;
            for(String userId : result.keySet()) {
                UserCoupon userCoupon = result.get(userId);
                if(idx < 3) {
                    assertThat(userCoupon).isNotNull();
                    assertThat(userCoupon.getUserId()).isEqualTo(userId);
                } else {
                    assertThat(userCoupon).isNull();
                }

                idx++;
            }
        }
    }
}
