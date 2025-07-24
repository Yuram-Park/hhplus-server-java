package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.CouponRepository;
import kr.hhplus.be.server.domain.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepositoryImpl implements CouponRepository {
    @Override
    public Coupon findByCouponType(char couponType) {
        return null;
    }

    @Override
    public Coupon updateByCouponType(char couponType, int couponInventory) {
        return null;
    }
}
