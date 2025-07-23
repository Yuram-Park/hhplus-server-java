package kr.hhplus.be.server.domain;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private int originalPaymentAmount;

    @Column(nullable = true)
    private Integer couponId;

    @Column(nullable = false)
    private int discountPaymentAmount;

    @Column(nullable = false)
    private int finalPaymentAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;

    public UserOrder() {}

    public UserOrder(Integer orderId, String userId, int originalPaymentAmount, Integer couponId, int discountPaymentAmount, int finalPaymentAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.originalPaymentAmount = originalPaymentAmount;
        this.couponId = couponId;
        this.discountPaymentAmount = discountPaymentAmount;
        this.finalPaymentAmount = finalPaymentAmount;
    }

    public UserOrder(String userId, int originalPaymentAmount, Integer couponId, int discountPaymentAmount, int finalPaymentAmount) {
        this.userId = userId;
        this.originalPaymentAmount = originalPaymentAmount;
        this.couponId = couponId;
        this.discountPaymentAmount = discountPaymentAmount;
        this.finalPaymentAmount = finalPaymentAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getOriginalPaymentAmount() {
        return originalPaymentAmount;
    }

    public void setOriginalPaymentAmount(int originalPaymentAmount) {
        this.originalPaymentAmount = originalPaymentAmount;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public int getDiscountPaymentAmount() {
        return discountPaymentAmount;
    }

    public void setDiscountPaymentAmount(int discountPaymentAmount) {
        this.discountPaymentAmount = discountPaymentAmount;
    }

    public int getFinalPaymentAmount() {
        return finalPaymentAmount;
    }

    public void setFinalPaymentAmount(int finalPaymentAmount) {
        this.finalPaymentAmount = finalPaymentAmount;
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

}
