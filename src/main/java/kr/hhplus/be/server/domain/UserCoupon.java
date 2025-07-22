package kr.hhplus.be.server.domain;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int couponId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private char couponType;

    @Column(nullable = false, columnDefinition = "boolean default FALSE")
    private boolean isUsed;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;

    public UserCoupon() {}

    public UserCoupon(int couponId, boolean isUsed) {
        this.couponId = couponId;
        this.isUsed = isUsed;
    }

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Business Logic

    /**
     * 쿠폰을 사용한다.
     * @param payment
     */
    public void useThisCoupon(int payment) {
        if(payment < 50_000) {
            // 쿠폰은 50,000원 이상 주문 시 사용 가능
            throw new IllegalArgumentException("쿠폰은 50,000원 이상 주문 시 사용할 수 있습니다.");
        }
        if(this.isUsed) {
            // 쿠폰은 한번만 사용 가능
            throw new IllegalArgumentException("이미 사용된 쿠폰입니다.");
        }
        this.isUsed = true;
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

}
