package kr.hhplus.be.server.application.jpa;

import kr.hhplus.be.server.domain.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Integer> {

    UserCoupon save(UserCoupon userCoupon);

    List<UserCoupon> findAllByUserId(String user_id);
}
