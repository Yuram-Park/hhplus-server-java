package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.application.service.UserCouponService;
import kr.hhplus.be.server.datasource.UserCouponRepositoryImpl;
import kr.hhplus.be.server.domain.UserCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserCouponService 테스트")
public class UserCouponServiceTest {

    @Mock
    private UserCouponRepositoryImpl userCouponRepository;

    @InjectMocks
    private UserCouponService userCouponService;

    @Nested
    @DisplayName("사용자 쿠폰 발급 시")
    class UserCouponIssue {

        @Test
        @DisplayName("사용자 쿠폰이 정상적으로 발급된다.")
        void 사용자_쿠폰발급_정상() {
            // given
            String userId = "user1";
            char couponType = 'A';
            UserCoupon userCoupon = new UserCoupon(1, userId, couponType, false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 1);

            when(userCouponRepository.createUserCoupon(any())).thenReturn(userCoupon);
            when(userCouponRepository.getUserCouponList(any())).thenReturn(new ArrayList<UserCoupon>(Arrays.asList(userCoupon)));

            // when
            userCouponService.createUserCoupon(userId, couponType);
            List<UserCoupon> result = userCouponService.getUserCouponList(userId);

            // then
            assertThat(result.get(0).getUserId()).isEqualTo(userId);
        }
    }
}
