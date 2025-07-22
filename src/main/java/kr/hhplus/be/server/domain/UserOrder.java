package kr.hhplus.be.server.domain;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private int orderQuantity;

    @Column(nullable = false)
    private int originalPaymentAmount;

    @Column(nullable = true)
    private int couponId;

    @Column(nullable = false)
    private int discountPaymentAmount;

    @Column(nullable = false)
    private int finalPaymentAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;

    public UserOrder() {}

    public UserOrder(int orderId, String userId, int orderQuantity, int originalPaymentAmount, int couponId, int discountPaymentAmount, int finalPaymentAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderQuantity = orderQuantity;
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

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
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

    public void setCouponId(int couponId) {
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
