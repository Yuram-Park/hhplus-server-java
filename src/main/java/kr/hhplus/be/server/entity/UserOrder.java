package kr.hhplus.be.server.entity;



public class UserOrder {
    private int orderId;
    private String userId;
    private int orderQuantity;
    private int originalPaymentAmount;
    private int couponId;
    private int discountPaymentAmount;
    private int finalPaymentAmount;

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
}
