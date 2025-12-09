package kr.hhplus.be.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PopularProductDto {
    private String productId;
    private String productName;
    private Long totalCount;
}
