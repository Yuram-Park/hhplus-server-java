package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.CouponRepository;
import kr.hhplus.be.server.application.jpa.CouponJpaRepository;
import kr.hhplus.be.server.domain.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Optional<Coupon> findByCouponType(char couponType) {
        return couponJpaRepository.findByCouponType(couponType);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }
}
