package kr.hhplus.be.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    private String productId;
    private int requestQuantity;
    private int productPayment; // 가격을 넣어주는게 맞을까? 주문리스트에 가격이 이미 표시되어있으니, 보내면 백에서 조회안해도되는..
    private boolean orderAvailability;
}
