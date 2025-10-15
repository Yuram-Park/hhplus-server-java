package kr.hhplus.be.server.application.service;

import kr.hhplus.be.server.datasource.UserCouponRepositoryImpl;
import kr.hhplus.be.server.domain.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private UserCouponRepositoryImpl userCouponRepository;

    /**
     * 사용자 쿠폰 목록 조회
     * @param userId
     * @return
     */
    public List<UserCoupon> getUserCouponList(String userId) {
        return userCouponRepository.getUserCouponList(userId);
    }

    /**
     * 사용자 쿠폰 정보 조회
     * @param couponId
     * @return
     */
    public UserCoupon getUserCouponInfo(int couponId) {
        return userCouponRepository.getUserCouponInfo(couponId).orElseThrow(() -> new NoSuchElementException(couponId + ": 해당하는 쿠폰이 없습니다."));
    }


    /**
     * 사용자 쿠폰 생성
     * @param userId
     * @param couponType
     * @return
     */
    public UserCoupon createUserCoupon(String userId, char couponType) {
        UserCoupon userCoupon = new UserCoupon(null, userId, couponType, false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), null);

        return userCouponRepository.createUserCoupon(userCoupon);
    }

    /**
     * 사용자 쿠폰 재고 차감
     * @param couponId
     * @param payment
     * @return
     */
    public UserCoupon reduceUserCoupon(int couponId, int payment) {
        UserCoupon userCoupon = userCouponRepository.getUserCouponInfo(couponId).orElseThrow(() -> new NoSuchElementException(couponId + ": 해당하는 쿠폰이 없습니다."));

        userCoupon.useThisCoupon(payment);

        return userCouponRepository.updateUserCoupon(userCoupon);
    }

}
