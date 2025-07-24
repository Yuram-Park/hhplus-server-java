package kr.hhplus.be.server.application.service;

import kr.hhplus.be.server.datasource.CouponRepositoryImpl;
import kr.hhplus.be.server.datasource.UserCouponRepositoryImpl;
import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.domain.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepositoryImpl couponRepository;

    private final UserCouponRepositoryImpl userCouponRepository;

    public Map<String, UserCoupon> issueCoupon(List<User> userList) {
        // 1등 A, 2등 B, 3등 C
        char[] couponTypes = {'A', 'B', 'C'};

        Map<String, UserCoupon> issuedList = new LinkedHashMap<>();

        for (int i = 0; i < userList.size(); i++) {
            if (i >= couponTypes.length) {
                issuedList.put(userList.get(i).getUserId(), null);
            } else {
                char couponType = couponTypes[i];
                Coupon coupon = couponRepository.findByCouponType(couponType);
                String userId = userList.get(i).getUserId();
                UserCoupon userCoupon = userCouponRepository.createUserCoupon(userId, coupon);
                issuedList.put(userList.get(i).getUserId(), userCoupon);

                // 쿠폰 재고 차감
                coupon.reduceCouponInventory();
                couponRepository.updateByCouponType(coupon.getCouponType(), coupon.getCouponInventory());
            }
        }

        return issuedList;
    }

}
