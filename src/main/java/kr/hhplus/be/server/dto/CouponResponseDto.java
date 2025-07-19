package kr.hhplus.be.server.dto;


/**
 * user_coupon 테이블과 coupon 테이블을 조인한 결과
 */
public class CouponResponseDto {

    // TODO join한 결과물을 담을 방법 찾는중
    private int couponId;
    private String userId;
    private char couponType;
    private boolean isUsed;
    private int couponDiscountPercent;

    public CouponResponseDto(int couponId, String userId, char couponType, boolean isUsed, int couponDiscountPercent) {
        this.couponId = couponId;
        this.userId = userId;
        this.couponType = couponType;
        this.isUsed = isUsed;
        this.couponDiscountPercent = couponDiscountPercent;
    }
}
