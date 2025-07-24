package kr.hhplus.be.server.application;

import kr.hhplus.be.server.domain.Coupon;

public interface CouponRepository {

    public Coupon findByCouponType(char couponType);
    public Coupon updateByCouponType(char couponType, int couponInventory);
}
