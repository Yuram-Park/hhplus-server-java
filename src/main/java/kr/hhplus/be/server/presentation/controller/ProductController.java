package kr.hhplus.be.server.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.service.ProductService;
import kr.hhplus.be.server.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
@Tag(name = "상품", description = "상품 관련 API")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 목록 조회
     * @return
     */
    @Operation(summary = "상품 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<List<Product>> getProductList(@RequestParam int page, @RequestParam int perPage) {
        List<Product> productList = productService.getProductList(page, perPage);
        return ResponseEntity.ok(productList);
    }

    /**
     * 상품 상세페이지 조회
     * @param productId
     * @return
     */
    @Operation(summary = "상품 상세페이지 조회")
    @GetMapping("/detail/{productId}")
    public ResponseEntity<Product> getProductDetail(@PathVariable String productId) {
        Product product = productService.getProductDetail(productId);
        return ResponseEntity.ok(product);
    }

    /**
     * 인기상품 목록 조회
     * @return
     */
    @Operation(summary = "인기상품 목록 조회")
    @GetMapping("/popular")
    public ResponseEntity<List<Product>> getPopularProductList() {
        List<Product> result = productService.loadDailyTopProducts();

        return ResponseEntity.ok(result);
    }

}
