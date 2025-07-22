package kr.hhplus.be.server.domain;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table
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

    public Product() {}

    public Product(String productId, String productName, String productDescription, int productInventory, int productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productInventory = productInventory;
        this.productPrice = productPrice;
    }

    public Product(String productId, int productInventory) {
        this.productId = productId;
        this.productInventory = productInventory;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductInventory() {
        return productInventory;
    }

    public void setProductInventory(int productInventory) {
        this.productInventory = productInventory;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
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
