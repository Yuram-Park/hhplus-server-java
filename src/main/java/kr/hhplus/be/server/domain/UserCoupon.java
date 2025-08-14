package kr.hhplus.be.server.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couponId;

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

    @Version
    private Integer version;

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
