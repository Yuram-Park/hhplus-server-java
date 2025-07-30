package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.UserCoupon;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {

    public UserCoupon createUserCoupon(UserCoupon userCoupon);

    public Optional<UserCoupon> getUserCouponInfo(int couponId);

    public List<UserCoupon> getUserCouponList(String userId);
}
