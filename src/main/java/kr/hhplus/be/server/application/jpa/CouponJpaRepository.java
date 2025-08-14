package kr.hhplus.be.server.application.jpa;

import kr.hhplus.be.server.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponJpaRepository extends JpaRepository<Coupon, Character> {

    Optional<Coupon> findByCouponType(char coupon_type);

    Coupon save(Coupon coupon);
}
