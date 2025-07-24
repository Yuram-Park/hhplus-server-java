package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.Coupon;

public interface CouponRepository {

    public Coupon findByCouponType(char couponType);
    public Coupon updateByCouponType(Coupon coupon);
}
