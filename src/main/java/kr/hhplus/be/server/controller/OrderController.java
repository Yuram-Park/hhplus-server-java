package kr.hhplus.be.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.dto.ProductRequestDto;
import kr.hhplus.be.server.entity.UserOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/order")
@Tag(name = "주문", description = "주문 관련 API")
public class OrderController {

    /**
     * 상품 주문 요청
     * @param productList
     * @return
     */
    @Operation(summary = "상품 주문 요청")
    @PostMapping("/requestOrder")
    public ResponseEntity<Boolean> requestOrder(@RequestBody List<ProductRequestDto> productList) {
        // TODO 사용자 토큰으로 userId 추출
        return ResponseEntity.ok(true);
    }

    /**
     * 상품 결제 요청
     * @param productList
     * @return
     */
    @Operation(summary = "상품 결제 요청")
    @PostMapping("/pay")
    public ResponseEntity<UserOrder>  createOrder(@RequestBody List<ProductRequestDto> productList) {
        // TODO 사용자 토큰으로 userId 추출
        return ResponseEntity.ok(new UserOrder(1, "1", 2, 55000, 0, 0, 55000));
    }
}
