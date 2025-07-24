package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.UserCouponRepository;
import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.UserCoupon;
import org.springframework.stereotype.Repository;

@Repository
public class UserCouponRepositoryImpl implements UserCouponRepository {
    @Override
    public UserCoupon createUserCoupon(String userId, Coupon coupon) {
        return null;
    }
}
