package kr.hhplus.be.server.presentation.facade;

import kr.hhplus.be.server.application.service.CouponService;
import kr.hhplus.be.server.application.service.UserCouponService;
import kr.hhplus.be.server.common.redis.DistributedLock;
import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.domain.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponService couponService;
    private final UserCouponService userCouponService;

    @DistributedLock(keyName = "'COUPON:' + #couponType")
    public UserCoupon issuedCoupon(User user, String couponType, int issueNum) {
        Coupon issuedCoupon = couponService.reduceCoupon(couponType, issueNum);
        return userCouponService.createUserCoupon(user.getUserId(), couponType);
    }
}
