package kr.hhplus.be.server.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
@Tag(name = "상품", description = "상품 관련 API")
public class ProductController {

    /**
     * 상품 목록 조회
     * @return
     */
    @Operation(summary = "상품 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<List<Product>> getProductList() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("T01", "White T-shirts", "하얀색 티셔츠", 3, 20000));
        productList.add(new Product("T02", "Long Jacket", "롱 자켓", 5, 35000));
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
        return ResponseEntity.ok(new Product("T02", "Long Jacket", "롱 자켓", 5, 35000));
    }

    /**
     * 인기상품 목록 조회
     * @return
     */
    @Operation(summary = "인기상품 목록 조회")
    @GetMapping("/popular")
    public ResponseEntity<List<Product>> getPopularProductList() {
        // TODO 현재 날짜를 받아 지정 기간동안의 상품목록 조회 로직 구현
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("T01", "White T-shirts", "하얀색 티셔츠", 3, 20000));
        productList.add(new Product("T02", "Long Jacket", "롱 자켓", 5, 35000));
        return ResponseEntity.ok(productList);
    }

}
