package kr.hhplus.be.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Coupon {

    @Id
    private String couponType;

    @Column(nullable = false)
    private int couponDiscountPercent;

    @Column(nullable = false)
    private int couponInventory;

    public void reduceCouponInventory(int reduceNum) {
        // 요청 차감 수는 0보다 커야 합니다.
        if(reduceNum <= 0) {
            throw new IllegalArgumentException("요청 차감 수는 0보다 커야 합니다.");
        }
        // 요청 차감 수는 재고 수보다 많아야 합니다.
        if(reduceNum > this.couponInventory) {
            throw new IllegalArgumentException("요청 차감 수는 쿠폰 재고 수보다 많아야 합니다.");
        }
        this.couponInventory -= reduceNum;
    }
}
