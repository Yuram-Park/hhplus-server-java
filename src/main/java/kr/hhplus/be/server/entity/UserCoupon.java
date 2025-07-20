package kr.hhplus.be.server.entity;



public class UserCoupon {
    private int couponId;
    private String userId;
    private char couponType;
    private boolean isUsed;

    public UserCoupon(int couponId, String userId, char couponType) {
        this.couponId = couponId;
        this.userId = userId;
        this.couponType = couponType;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public char getCouponType() {
        return couponType;
    }

    public void setCouponType(char couponType) {
        this.couponType = couponType;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}
