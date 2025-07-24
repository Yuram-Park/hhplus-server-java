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
public class Product {

    @Id
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String productDescription;

    @Column(nullable = false)
    private int productInventory;

    @Column(nullable = false)
    private int productPrice;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;

    // Business Logic

    public void reduceInventory(int reduceCount) {
        if(reduceCount <= 0) {
            // 요청 수량은 0보다 커야합니다.
            throw new IllegalArgumentException("요청 수량은 0보다 커야합니다.");
        }
        if(this.productInventory < reduceCount) {
            // 요청 수량은 재고 수량보다 많을 수 없습니다.
            throw new IllegalArgumentException("요청 수량은 재고 수량보다 많을 수 없습니다.");
        }
        this.productInventory -= reduceCount;
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

}
