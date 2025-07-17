package kr.hhplus.be.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.dto.CouponResponseDto;
import kr.hhplus.be.server.entity.UserCoupon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/coupon")
@Tag(name = "쿠폰", description = "쿠폰 관련 API")
public class CouponController {

    /**
     * 보유 쿠폰 조회
     * @return
     */
    @Operation(summary = "보유 쿠폰 조회")
    @GetMapping("/myCoupon")
    public ResponseEntity<List<CouponResponseDto>> getMyCoupons() {
        // TODO 사용자 토큰으로 userId 추출
        // TODO userId로 쿠폰 리스트 조회
        List<CouponResponseDto> couponList = new ArrayList<>();
        couponList.add(new CouponResponseDto(1, "1", 'A', false, 10));
        couponList.add(new CouponResponseDto(2, "1", 'B', false, 20));
        return ResponseEntity.ok(couponList);
    }

    /**
     * 선착순 쿠폰 발급
     * @return
     */
    @Operation(summary = "선착순 쿠폰 발급")
    @PostMapping("/issue")
    public ResponseEntity<Boolean> issueCoupon() {
        // TODO 선착순 로직 필요..
        return ResponseEntity.ok(true);
    }

}
