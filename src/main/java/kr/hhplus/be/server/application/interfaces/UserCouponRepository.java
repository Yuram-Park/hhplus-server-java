package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.UserCoupon;

public interface UserCouponRepository {

    public UserCoupon createUserCoupon(UserCoupon userCoupon);
}
