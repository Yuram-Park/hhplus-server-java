package kr.hhplus.be.server.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.service.OrderService;
import kr.hhplus.be.server.domain.Product;
import kr.hhplus.be.server.dto.ProductRequestDto;
import kr.hhplus.be.server.domain.Order;
import kr.hhplus.be.server.presentation.facade.OrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/order")
@Tag(name = "주문", description = "주문 관련 API")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderFacade orderFacade;

    /**
     * 상품 주문 요청
     * @param productList
     * @return
     */
    @Operation(summary = "상품 주문 요청")
    @PostMapping("/requestOrder")
    public ResponseEntity<Order> requestOrder(@RequestBody List<ProductRequestDto> productList, String userId) {
        return ResponseEntity.ok(orderFacade.requestOrder(productList, userId));
    }

    /**
     * 상품 결제 요청
     * @param productList
     * @return
     */
    @Operation(summary = "상품 결제 요청")
    @PostMapping("/pay")
    public ResponseEntity<Order>  createOrder(@RequestParam String userId, @RequestBody Map<ProductRequestDto, Product> productList) {
        return ResponseEntity.ok(orderFacade.requestPayment(userId, productList));
    }
}
