package kr.hhplus.be.server.presentation.facade;

import kr.hhplus.be.server.application.service.CouponService;
import kr.hhplus.be.server.application.service.UserCouponService;
import kr.hhplus.be.server.application.service.UserService;
import kr.hhplus.be.server.common.redis.DistributedLock;
import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.domain.UserCoupon;
import kr.hhplus.be.server.dto.CouponRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponService couponService;
    private final UserCouponService userCouponService;
    private final UserService userService;

    @DistributedLock(keyName = "'COUPON:' + #couponType")
    public UserCoupon issuedCoupon(User user, String couponType, int issueNum) {
        Coupon issuedCoupon = couponService.reduceCoupon(couponType, issueNum);
        return userCouponService.createUserCoupon(user.getUserId(), couponType);
    }

    /**
     * 선착순 대기열에서 당첨자 정보를 가져와서 쿠폰 발행
     * @param size
     */
    @Transactional
    public boolean issueCouponFromQueue(int size) {
        // Redis에서 score가 가장 낮은 size개의 리스트를 반환하고 redis에서 제거
        List<CouponRequestDto> selectedList = couponService.releaseListFromQueue(size);

        selectedList.forEach(list -> {
            try {
                User user = userService.getUser(list.getUserId()).orElseThrow(); // TODO 예외 처리 필요
                Coupon coupon = couponService.getCouponInfo(list.getCouponType()); // TODO 예회 처리 필요

                // 해당 User에 해당 쿠폰 발급
                couponService.reduceCoupon(coupon.getCouponType(), 1); // 재고 감소
                userCouponService.createUserCoupon(user.getUserId(), coupon.getCouponType());
            } catch (Exception e) {
                // 중간에 멈추면 안됨으로 예외 발생시 로그 기록
                log.error("Failed to issue coupon for userId : {}, couponType : {}", list.getUserId(), list.getCouponType());
            }
        });

        return true; // TODO return type 고민 필요. resultDto 만들어서 return하기
    }
}
