package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.Coupon;

import java.util.Optional;

public interface CouponRepository {

    // 재고 관리용 Lock 메서드
    Optional<Coupon> findByCouponTypeWithLock(String couponType);

    Optional<Coupon> findByCouponType(String couponType);
    Coupon updateByCouponType(Coupon coupon);
}
