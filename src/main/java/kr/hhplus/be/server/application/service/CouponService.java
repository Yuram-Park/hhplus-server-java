package kr.hhplus.be.server.application.service;

import kr.hhplus.be.server.common.redis.DistributedLock;
import kr.hhplus.be.server.common.utils.CacheNames;
import kr.hhplus.be.server.datasource.CouponRepositoryImpl;
import kr.hhplus.be.server.datasource.UserCouponRepositoryImpl;
import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.domain.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepositoryImpl couponRepository;

    private final UserCouponRepositoryImpl userCouponRepository;

    /**
     * 쿠폰 종류 생성
     * @param coupon
     * @return
     */
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.findByCouponType(coupon.getCouponType()).orElseGet(() -> couponRepository.updateByCouponType(coupon));
    }

    /**
     * 쿠폰 재고 차감
     * @param couponType
     * @param reduceNum
     * @return
     */
    @Transactional
    public Coupon reduceCoupon(String couponType, int reduceNum) {
        Coupon coupon = couponRepository.findByCouponTypeWithLock(couponType).orElseThrow(() -> new NoSuchElementException(couponType + ": 해당하는 쿠폰이 없습니다."));
        coupon.reduceCouponInventory(reduceNum);
        return couponRepository.updateByCouponType(coupon);
    }

    /**
     * 선착순 쿠폰 발행
     * @param userList
     * @return
     */
    public Map<String, UserCoupon> issueFcfsCoupon(List<User> userList) {
        // 1등 A, 2등 B, 3등 C
        String[] couponTypes = {"A", "B", "C"};

        Map<String, UserCoupon> issuedList = new LinkedHashMap<>();

        for (int i = 0; i < userList.size(); i++) {
            if (i >= couponTypes.length) {
                issuedList.put(userList.get(i).getUserId(), null);
            } else {
                String couponType = couponTypes[i];
                Coupon coupon = couponRepository.findByCouponTypeWithLock(couponType).orElseThrow();
                String userId = userList.get(i).getUserId();
                UserCoupon userCoupon = new UserCoupon(null, userId, coupon.getCouponType(), false, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), 0);
                UserCoupon userCouponResult = userCouponRepository.createUserCoupon(userCoupon);
                issuedList.put(userList.get(i).getUserId(), userCouponResult);

                // 쿠폰 재고 차감
                coupon.reduceCouponInventory(1);
                couponRepository.updateByCouponType(coupon);
            }
        }
        return issuedList;
    }

    /**
     * 쿠폰 정보 검색
     * @param couponType
     * @return
     */
    @Cacheable(value = CacheNames.COUPON_TYPE, key = "#couponType")
    @Transactional(readOnly = true)
    public Coupon getCouponInfo(String couponType) {
        return couponRepository.findByCouponType(couponType).orElseThrow(() -> new NoSuchElementException(couponType + ": 해당하는 쿠폰이 없습니다."));
    }

    /**
     * 사용자 쿠폰 정보 검색 -> UserCouponService로 옮겨야함
     * @param couponId
     * @return
     */
    public UserCoupon useCoupon(int couponId) {
        return userCouponRepository.getUserCouponInfo(couponId).orElseThrow(() -> new NoSuchElementException(couponId + " : 해당하는 쿠폰이 없습니다."));
    }

    /**
     * 보유 쿠폰 리스트 검색
     * @param userId
     * @return
     */
    public List<UserCoupon> getCouponList(String userId) {
        return userCouponRepository.getUserCouponList(userId);
    }



}
