package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.UserCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("UserCoupon 도메인 테스트")
public class UserCouponTest {

    @Nested
    @DisplayName("쿠폰 사용 시")
    class useCoupon {

        @Test
        @DisplayName("쿠폰이 정상적으로 사용된다.")
        void 쿠폰_사용_성공() {
            // given
            UserCoupon userCoupon = new UserCoupon(1, "ID01", "A", false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0);

            // when
            userCoupon.useThisCoupon(50_000);

            // then
            assertThat(userCoupon.isUsed()).isEqualTo(true);
        }

        @Test
        @DisplayName("50,000원 미만 주문 시 사용할 수 없다.")
        void 오만원_미만_사용_불가() {
            // given
            UserCoupon userCoupon  = new UserCoupon(1, "ID01", "A", false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> userCoupon.useThisCoupon(49999));
        }

        @Test
        @DisplayName("사용한 쿠폰은 사용할 수 없다.")
        void 사용_쿠폰_사용_불가() {
            // given
            UserCoupon userCoupon = new UserCoupon(1, "ID01", "A", false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0);
            userCoupon.useThisCoupon(50000);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> userCoupon.useThisCoupon(50000));
        }
    }
}
