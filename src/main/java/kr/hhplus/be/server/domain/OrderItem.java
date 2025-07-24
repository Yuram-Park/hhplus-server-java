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
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

    @Column(nullable = false)
    private int orderId;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private int orderItemQuantity;

    @Column(nullable = false)
    private int orderItemPayment;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedAt;


}
