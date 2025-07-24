package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.CouponRepository;
import kr.hhplus.be.server.application.jpa.CouponJpaRepository;
import kr.hhplus.be.server.domain.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Coupon findByCouponType(char couponType) {
        return couponJpaRepository.findByCouponType(couponType);
    }

    @Override
    public Coupon updateByCouponType(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }
}
