package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.Coupon;

import java.util.Optional;

public interface CouponRepository {

    Optional<Coupon> findByCouponType(char couponType);
    Coupon save(Coupon coupon);
}
