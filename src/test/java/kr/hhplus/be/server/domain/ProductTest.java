package kr.hhplus.be.server.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Product 도메인 테스트")
public class ProductTest {

    @Nested
    @DisplayName("상품 재고 차감 시")
    class ReduceInventory {

        @Test
        @DisplayName("재고가 정상적으로 차감된다.")
        void 재고_차감_성공() {
            // given
            int reduceCount = 2;
            Product product = new Product("T01", 5);

            // when
            product.reduceInventory(reduceCount);

            // then
            assertThat(product.getProductInventory()).isEqualTo(3);
        }

        @Test
        @DisplayName("요청 차감 수량은 0 이하일 수 없다.")
        void 영개_이하_요청_불가() {
            // given
            int reduceCount = 0;
            Product product = new Product("T01", 5);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> product.reduceInventory(reduceCount));
        }

        @Test
        @DisplayName("요청 수량은 재고량보다 많을 수 없다.")
        void 재고량_초과_요청_불가() {
            // given
            int reduceCount = 6;
            Product product = new Product("T01", 5);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> product.reduceInventory(reduceCount));
        }
    }
}
