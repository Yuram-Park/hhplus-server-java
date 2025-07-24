package kr.hhplus.be.server.service;

import kr.hhplus.be.server.application.service.CouponService;
import kr.hhplus.be.server.datasource.CouponRepositoryImpl;
import kr.hhplus.be.server.datasource.UserCouponRepositoryImpl;
import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.domain.UserCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CouponService 테스트")
public class CouponServiceTest {

    @Mock
    private CouponRepositoryImpl couponRepository;

    @Mock
    private UserCouponRepositoryImpl userCouponRepository;

    @InjectMocks
    private CouponService couponService;

    @Nested
    @DisplayName("쿠폰 발급 시")
    class CouponIssue {

        @Test
        @DisplayName("선착순으로 user coupon이 발급된다.")
        void 선착순_쿠폰_발급_성공() {
            // given
            String userId1 = "ID01";
            String userId2 = "ID02";
            String userId3 = "ID03";
            List<User> userList = new ArrayList<>();
            userList.add(new User(userId1, "1111", null, null, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            userList.add(new User(userId2, "1111", null, null, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            userList.add(new User(userId3, "1111", null, null, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

            when(couponRepository.findByCouponType('A')).thenReturn(new Coupon('A', 30, 5));
            when(couponRepository.findByCouponType('B')).thenReturn(new Coupon('B', 20, 4));
            when(couponRepository.findByCouponType('C')).thenReturn(new Coupon('C', 10, 3));
            when(userCouponRepository.createUserCoupon(eq(userId1), any(Coupon.class))).thenReturn(new UserCoupon(1, userId1, 'A', false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            when(userCouponRepository.createUserCoupon(eq(userId2), any(Coupon.class))).thenReturn(new UserCoupon(2, userId2, 'B', false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            when(userCouponRepository.createUserCoupon(eq(userId3), any(Coupon.class))).thenReturn(new UserCoupon(3, userId3, 'C', false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            when(couponRepository.updateByCouponType(anyChar(), anyInt())).thenReturn(new Coupon());

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
            String userId1 = "ID01";
            String userId2 = "ID02";
            String userId3 = "ID03";
            String userId4 = "ID04";
            List<User> userList = new ArrayList<>();
            userList.add(new User(userId1, "1111", null, null, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            userList.add(new User(userId2, "1111", null, null, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            userList.add(new User(userId3, "1111", null, null, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            userList.add(new User(userId4, "1111", null, null, 0, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

            when(couponRepository.findByCouponType('A')).thenReturn(new Coupon('A', 30, 5));
            when(couponRepository.findByCouponType('B')).thenReturn(new Coupon('B', 20, 4));
            when(couponRepository.findByCouponType('C')).thenReturn(new Coupon('C', 10, 3));
            when(userCouponRepository.createUserCoupon(eq(userId1), any(Coupon.class))).thenReturn(new UserCoupon(1, userId1, 'A', false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            when(userCouponRepository.createUserCoupon(eq(userId2), any(Coupon.class))).thenReturn(new UserCoupon(2, userId2, 'B', false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            when(userCouponRepository.createUserCoupon(eq(userId3), any(Coupon.class))).thenReturn(new UserCoupon(3, userId3, 'C', false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            when(couponRepository.updateByCouponType(anyChar(), anyInt())).thenReturn(new Coupon());

            // when
            Map<String, UserCoupon> result = couponService.issueCoupon(userList);

            // then
            int idx = 0;
            for(String userId : result.keySet()) {
                UserCoupon userCoupon = result.get(userId);
                if(idx < 3) {
                    assertThat(userCoupon.getUserId()).isEqualTo(userId);
                } else {
                    assertThat(userCoupon).isEqualTo(null);
                }

                idx++;
            }
        }
    }
}
