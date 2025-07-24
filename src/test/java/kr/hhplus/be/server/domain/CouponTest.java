package kr.hhplus.be.server.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Coupon 도메인 테스트")
public class CouponTest {

    @Test
    @DisplayName("쿠폰 발급 시 쿠폰 재고를 차감한다.")
    void 쿠폰_발급_성공() {
        // given
        int couponInventory = 1;
        Coupon coupon = new Coupon('A', 30, couponInventory);

        // when
        coupon.reduceCouponInventory();

        // then
        assertThat(coupon.getCouponInventory()).isEqualTo(couponInventory - 1);
    }
}
