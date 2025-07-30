package kr.hhplus.be.server.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.service.CouponService;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.domain.UserCoupon;
import kr.hhplus.be.server.dto.CouponResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coupon")
@Tag(name = "쿠폰", description = "쿠폰 관련 API")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

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
    public ResponseEntity<Map<String, UserCoupon>> issueCoupon(List<User> userList) {
        return ResponseEntity.ok(couponService.issueCoupon(userList));
    }

}
