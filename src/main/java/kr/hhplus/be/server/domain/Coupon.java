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
    private char couponType;

    @Column(nullable = false)
    private int couponDiscountPercent;

    @Column(nullable = false)
    private int couponInventory;

    public void reduceCouponInventory() {
        this.couponInventory -= 1;
    }
}
