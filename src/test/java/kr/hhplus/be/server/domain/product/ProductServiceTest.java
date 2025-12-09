package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.application.service.ProductService;
import kr.hhplus.be.server.datasource.ProductRepositoryImpl;
import kr.hhplus.be.server.domain.Product;
import kr.hhplus.be.server.dto.PopularProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 테스트")
public class ProductServiceTest {

    @Mock
    private ProductRepositoryImpl productRepository;

    @InjectMocks
    private ProductService productService;

    @Nested
    @DisplayName("상품 조회 시")
    class GetProduct {

        @Test
        @DisplayName("상품 리스트를 조회 할 수 있다.")
        void 상품_리스트_조회_성공() {
            // given
            int page = 1;
            int perPage = 3;

            List<Product> productList = new ArrayList<>();
            productList.add(new Product("T01","티셔츠", "하얀색 티셔츠", 10, 10_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            productList.add(new Product("B01","청바지", "청바지", 5, 13_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            productList.add(new Product("O01","원피스", "뷔스티에 원피스", 6, 20_000, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));
            Page<Product> pageResults = new PageImpl<>(productList);
            when(productRepository.findByPagePerPage(any())).thenReturn(pageResults);

            // when
            List<Product> result = productService.getProductList(page, perPage);

            // then
            assertThat(result).extracting("productId", "productName", "productDescription", "productInventory", "productPrice")
                    .containsExactlyInAnyOrder(
                            tuple("T01","티셔츠", "하얀색 티셔츠", 10, 10_000),
                            tuple("B01","청바지", "청바지", 5, 13_000),
                            tuple("O01","원피스", "뷔스티에 원피스", 6, 20_000)
                    );
        }

        @Test
        @DisplayName("상품 상세 정보를 조회 할 수 있다.")
        void 상품_상세_정보_조회() {
            // given
            String productId = "T01";
            String productName = "티셔츠";
            String productDescription = "하얀색 티셔츠";
            int productPrice = 10_000;
            int originalInventory = 10;

            Product product = new Product(productId, productName, productDescription, originalInventory, productPrice, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            when(productRepository.findByProductId(anyString())).thenReturn(Optional.of(product));

            // when
            Product result = productService.getProductDetail(productId);

            // then
            assertThat(result).extracting("productId", "productName", "productDescription", "productInventory", "productPrice")
                    .containsExactlyInAnyOrder(productId, productName, productDescription, originalInventory, productPrice);
        }

        @Test
        @DisplayName("단일 상품 재고를 조회 할 수 있다.")
        void 단일_상품_재고_조회_성공() {
            // given
            String productId = "T01";
            String productName = "티셔츠";
            String productDescription = "하얀색 티셔츠";
            int productPrice = 10_000;
            int originalInventory = 10;

            Product product = new Product(productId, productName, productDescription, originalInventory, productPrice, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            when(productRepository.findByProductId(anyString())).thenReturn(Optional.of(product));

            // when
            Product result = productService.getProductDetail(productId);

            // then
            assertThat(result.getProductInventory()).isEqualTo(product.getProductInventory());
        }

        @Test
        @DisplayName("당일 인기상품을 조회할 수 있다.")
        void 당일_인기상품_조회_성공() {
            // given
            PopularProductDto p1 = new PopularProductDto("T01", "티셔츠", 100L);
            PopularProductDto p2 = new PopularProductDto("P01", "청바지츠", 150L);
            PopularProductDto p3 = new PopularProductDto("S01", "스커트", 200L);

            List<PopularProductDto> popularProductDtos = List.of(p1, p2, p3);

            when(productRepository.findPopularProduct(any())).thenReturn(popularProductDtos);

            // when
            List<PopularProductDto> result = productService.findPopularProducts(LocalDate.now());

            // then
            assertThat(result).isEqualTo(popularProductDtos);
        }
    }

    @Nested
    @DisplayName("상품 결제 시")
    class PayProduct {

        @Test
        @DisplayName("상품 재고가 차감된다.")
        void 상품재고_차감_성공() {
            // given
            String productId = "T01";
            String productName = "티셔츠";
            String productDescription = "하얀색 티셔츠";
            int productPrice = 10_000;
            int originalInventory = 10;
            int reduceInventory = 6;

            Product product = new Product(productId, productName, productDescription, originalInventory, productPrice, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            when(productRepository.findByProductId(anyString())).thenReturn(Optional.of(product));
            when(productRepository.updateByProductId(any())).thenReturn(new Product(productId, productName, productDescription, originalInventory - reduceInventory, productPrice, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

            // when
            product = productService.reduceProduct(product.getProductId(), reduceInventory);

            // then
            assertThat(product.getProductInventory()).isEqualTo(originalInventory - reduceInventory);
        }

        @Test
        @DisplayName("요청 수량은 0개 이하일 수 없다.")
        void 영개_이하_실패() {
            // given
            String productId = "T01";
            String productName = "티셔츠";
            String productDescription = "하얀색 티셔츠";
            int productPrice = 10_000;
            int originalInventory = 10;
            int reduceInventory = 0;

            Product product = new Product(productId, productName, productDescription, originalInventory, productPrice, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            when(productRepository.findByProductId(anyString())).thenReturn(Optional.of(product));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> productService.reduceProduct(product.getProductId(), reduceInventory));
        }

        @Test
        @DisplayName("재고수량보다 요청수량이 많을 수 없다.")
        void 재고_초과_요청_실패() {
            // given
            String productId = "T01";
            String productName = "티셔츠";
            String productDescription = "하얀색 티셔츠";
            int productPrice = 10_000;
            int originalInventory = 10;
            int reduceInventory = 11;

            Product product = new Product(productId, productName, productDescription, originalInventory, productPrice, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            when(productRepository.findByProductId(anyString())).thenReturn(Optional.of(product));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> productService.reduceProduct(product.getProductId(), reduceInventory));

        }
    }
}
