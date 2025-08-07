package kr.hhplus.be.server.presentation.facade;

import kr.hhplus.be.server.application.service.OrderService;
import kr.hhplus.be.server.application.service.PointService;
import kr.hhplus.be.server.application.service.ProductService;
import kr.hhplus.be.server.domain.Order;
import kr.hhplus.be.server.domain.Product;
import kr.hhplus.be.server.domain.User;
import kr.hhplus.be.server.dto.ProductRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final ProductService productService;
    private final PointService pointService;
    private final OrderService orderService;

    /**
     * 상품 주문 요청
     * @param productList
     * @return
     */
    public Map<ProductRequestDto, Product> requestOrder(List<ProductRequestDto> productList) {

        Map<ProductRequestDto, Product> results = new HashMap<>();

        for (ProductRequestDto productDto : productList) {
            // 상품 재고 조회 요청
            Product product = productService.getProductDetail(productDto.getProductId());

            if (productDto.getRequestQuantity() <= product.getProductInventory()) {
                productDto.setOrderAvailability(true);
            } else {
                productDto.setOrderAvailability(false);
            }

            results.put(productDto, product);
        }
        // TODO OrderItem 리스트와 orderId=null인 Order 를 return해주기
        return results;
    }

    /**
     * 상품 결제 요청
     * @param userId
     * @param productList
     * @return
     */
//    @Transactional
    public Order requestPayment(String userId, Map<ProductRequestDto, Product> productList) {

        int totalPrice = 0;
        // 총 결제 금액 계산
        for(ProductRequestDto dto : productList.keySet()) {
            Product product = productList.get(dto);
            totalPrice += product.getProductPrice();
        }

        // 잔액 확인 요청
        User user = pointService.getUserPoint(userId);

        // 주문금액 <= 잔액 : 주문 가능
        if(totalPrice <= user.getUserPoint()) {

            // 상품 재고 차감 요청
            for(ProductRequestDto dto : productList.keySet()) {
                Product product = productList.get(dto);
                productService.reduceProduct(product.getProductId(), dto.getRequestQuantity());
            }

            // TODO 쿠폰 적용 요청
            Integer couponId = null;
            int discountPaymentAmount = 0;

            int finalPaymentAmount = totalPrice - discountPaymentAmount;

            // 포인트 사용(결제) 요청
            try {
                pointService.usePoint(user.getUserId(), totalPrice);
            } catch (Exception e) {
                // 결제 실패 시 상품 재고 복구
                for(ProductRequestDto dto : productList.keySet()) {
                    Product product = productList.get(dto);
                    productService.increaseProduct(product.getProductId(), dto.getRequestQuantity());
                }
            }

            // 주문내역 생성 요청
            Order order = new Order(null, user.getUserId(), totalPrice, couponId, discountPaymentAmount, finalPaymentAmount, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            Order result = orderService.createOrder(order);
            return result;
        } else {
            // 주문금액 > 잔액 : 주문 불가능
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

    }


}
