package kr.hhplus.be.server.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.service.CouponService;
import kr.hhplus.be.server.domain.Coupon;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.domain.UserCoupon;
import kr.hhplus.be.server.dto.CouponRequestDto;
import kr.hhplus.be.server.dto.CouponResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coupon")
@Tag(name = "쿠폰", description = "쿠폰 관련 API")
@RequiredArgsConstructor
@Slf4j
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 종류 정보 조회
     * @param couponType
     * @return
     */
    @GetMapping("/couponType/{couponType}")
    public ResponseEntity<Coupon> getCouponTypeInfo(@PathVariable String couponType) {
        long startTime = System.currentTimeMillis();
        Coupon couponResult = couponService.getCouponInfo(couponType);
        long endTime = System.currentTimeMillis();

        log.info("[쿠폰 상세 조회] Response Time : {}ms", (endTime - startTime));
        return ResponseEntity.ok(couponResult);
    }

    /**
     * 보유 쿠폰 조회
     * @param userId
     * @return
     */
    @Operation(summary = "보유 쿠폰 조회")
    @GetMapping("/myCoupon")
    public ResponseEntity<List<UserCoupon>> getMyCoupons(@RequestParam String userId) {
        List<UserCoupon> couponList = couponService.getCouponList(userId);
        return ResponseEntity.ok(couponList);
    }

    /**
     * 선착순 쿠폰 발급
     * @param userList
     * @return
     */
    @Operation(summary = "선착순 쿠폰 발급")
    @PostMapping("/issue")
    public ResponseEntity<Map<String, UserCoupon>> issueCoupon(CouponRequestDto requestDto) {
        return ResponseEntity.ok(couponService.issueFcfsCoupon(userList));
    }

}
