package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.UserCouponRepository;
import kr.hhplus.be.server.application.jpa.UserCouponJpaRepository;
import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final UserCouponJpaRepository userCouponJpaRepository;

    @Override
    public UserCoupon createUserCoupon(UserCoupon userCoupon) {
        return userCouponJpaRepository.save(userCoupon);
    }

    @Override
    public Optional<UserCoupon> getUserCouponInfo(int couponId) {
        return userCouponJpaRepository.findById(couponId);
    }

    @Override
    public List<UserCoupon> getUserCouponList(String userId) {
        return userCouponJpaRepository.findAllByUserId(userId);
    }

    @Override
    public UserCoupon updateUserCoupon(UserCoupon userCoupon) {
        return userCouponJpaRepository.save(userCoupon);
    }


}
