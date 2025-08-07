package kr.hhplus.be.server.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "`user`")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private String userId;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = true)
    private String userPhoneNumber;

    @Column(nullable = true)
    private String userAddress;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int userPoint;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;

    @Version
    private Integer version;

    // Business Logic

    /**
     * 포인트를 충전한다.
     * @param amount
     */
    public void chargePoint(int amount) {
        if(amount <= 0) {
            // 충전 금액은 0보다 커야 합니다.
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        int tempUserPoint = this.userPoint;
        this.userPoint += amount;

        if(this.userPoint > 100_000) {
            // 100,000원 까지만 충전이 가능합니다.
            this.userPoint = tempUserPoint;
            throw new IllegalArgumentException("100,000원 까지만 충전이 가능합니다.");
        }

        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    /**
     * 포인트를 사용한다.
     * @param amount
     */
    public void usePoint(int amount) {
        if(amount <= 0){
            // 사용 금액은 0보다 커야 합니다.
            throw new IllegalArgumentException("사용 금액은 0보다 커야 합니다.");
        }
        if(this.userPoint < amount) {
            // 보유 포인트보다 많은 금액을 사용할 수 없습니다.
            throw new IllegalArgumentException("보유 포인트보다 많은 금액을 사용할 수 없습니다.");
        }

        this.userPoint -= amount;
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
