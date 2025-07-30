package kr.hhplus.be.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    private String productId;
    private int requestQuantity;
    private boolean orderAvailability;
}
