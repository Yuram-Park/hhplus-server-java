package kr.hhplus.be.server.presentation.facade;

import kr.hhplus.be.server.application.service.*;
import kr.hhplus.be.server.domain.*;
import kr.hhplus.be.server.dto.ProductRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserCouponService userCouponService;

    /**
     * 상품 주문 요청
     *
     * @param productList
     * @return
     */
    public Order requestOrder(List<ProductRequestDto> productList, String userId) {

        // 가격 총합 계산
        int totalPayment = productList.stream().mapToInt(ProductRequestDto::getProductPayment).sum();
        // orderId=null 인 Order 객체 생성
        return new Order(null, userId, totalPayment, null, 0, totalPayment, null, null);

    }

    /**
     * 상품 결제 요청
     * @param userId
     * @param productList
     * @return
     */
//    @Transactional
    public Order requestPayment(List<ProductRequestDto> productList, Order order, String userId) {

        // 쿠폰 사용 정보, 주소가 입력된 Order 객체 및 상품주문 리스트를 받아 진행

        // 잔액 확인 요청
        User user = userService.getUser(userId).orElseThrow();

        // 주문금액 <= 잔액 : 주문 가능
        int totalPrice = order.getFinalPaymentAmount();
        if(totalPrice <= user.getUserPoint()) {

//            List<Product> orderProductList = new ArrayList<>();

            // 상품 재고 차감 요청
            for(ProductRequestDto dto : productList) {
                // 상품 정보 조회
                Product product = productService.getProductDetail(dto.getProductId());
//                orderProductList.add(product);
                productService.reduceProduct(product.getProductId(), dto.getRequestQuantity());
            }

            // 쿠폰 사용
            userCouponService.useUserCoupon(order.getCouponId(), order.getOriginalPaymentAmount());

            // 포인트 사용(결제) 요청
            try {
                userService.usePoint(user.getUserId(), totalPrice);
            } catch (Exception e) {
                // 결제 실패 시 상품 재고 복구
                for(ProductRequestDto dto : productList) {
                    Product product = productService.getProductDetail(dto.getProductId());
                    productService.increaseProduct(product.getProductId(), dto.getRequestQuantity());
                }
                // 쿠폰 사용여부 복구
                userCouponService.cancleUserCouponUse(order.getOrderId());
            }

            // 주문내역 생성 요청
            Order result = orderService.createOrder(order);

            int orderId = result.getOrderId();

            // orderId로 orderItems 생성
            List<OrderItem> orderItemList = new ArrayList<>();
            for(ProductRequestDto dto : productList) {
                OrderItem orderItem = new OrderItem(null, orderId, dto.getProductId(), dto.getRequestQuantity(), dto.getProductPayment(),new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
                orderItemList.add(orderItem);
            }
            orderItemService.saveAllOrderItems(orderId, orderItemList);

            return result;

        } else {
            // 주문금액 > 잔액 : 주문 불가능
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

    }


}
