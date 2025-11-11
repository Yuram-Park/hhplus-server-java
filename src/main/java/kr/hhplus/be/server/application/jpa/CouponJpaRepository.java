package kr.hhplus.be.server.application.jpa;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponJpaRepository extends JpaRepository<Coupon, Character> {

    // 재고 관리용 Lock 메서드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.couponType = :couponType")
    Optional<Coupon> findByCouponTypeWithLock(@Param("couponType") String couponType);

    Optional<Coupon> findByCouponType(String coupon_type);

    Coupon save(Coupon coupon);
}
