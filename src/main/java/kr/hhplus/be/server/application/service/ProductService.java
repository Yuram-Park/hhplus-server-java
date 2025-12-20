package kr.hhplus.be.server.application.service;

import kr.hhplus.be.server.application.interfaces.DailySalesProductRepository;
import kr.hhplus.be.server.application.interfaces.ProductRepository;
import kr.hhplus.be.server.common.utils.CacheNames;
import kr.hhplus.be.server.datasource.ProductRepositoryImpl;
import kr.hhplus.be.server.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final DailySalesProductRepository dailySalesProductRepository;

    /**
     * 상품 리스트 조회
     * @param page
     * @param perPage
     * @return
     */
    public List<Product> getProductList(int page, int perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        Page<Product> pageResult = productRepository.findByPagePerPage(pageable);
        return pageResult.getContent();
    }

    /**
     * 상품 상세 조회
     * @param productId
     * @return
     */
    @Transactional(readOnly = true)
    public Product getProductDetail(String productId) {
        return productRepository.findByProductId(productId)
                .orElseThrow(() -> new NoSuchElementException(productId + " : 해당 상품이 존재하지 않습니다."));
    }

    /**
     * 상품 재고 차감
     * @param productId
     * @param reduceCount
     * @return
     */
    @Transactional
    public Product reduceProduct(String productId, int reduceCount) {
        Product product = productRepository.findByProductIdWithLock(productId).orElseThrow(() -> new NoSuchElementException(productId + " : 해당 상품이 존재하지 않습니다."));
        product.reduceInventory(reduceCount);
        return productRepository.updateByProductId(product);
    }

    /**
     * 상품 재고 증가
     * @param productId
     * @param increaseCount
     * @return
     */
    public Product increaseProduct(String productId, int increaseCount) {
        Product product = productRepository.findByProductId(productId).orElseThrow(() -> new NoSuchElementException(productId + " : 해당 상품이 존재하지 않습니다."));
        product.increaseInventory(increaseCount);
        return productRepository.updateByProductId(product);
    }

    /**
     * 인기 상품 조회(기존 로직)
     * @param startDate
     * @return
     */
    public List<Product> findPopularProducts(LocalDate startDate) {
        return productRepository.findPopularProduct(startDate);
    }

    /**
     * 전일 인기 상품 조회를 위한 Redis 업데이트
     * @return
     */
    @CachePut(value = CacheNames.PRODUCT_SALES, key = "'DAILY_PRODUCT_SALES:' + T(java.time.LocalDate).now().toString()")
    @Transactional(readOnly = true)
    public List<Product> refreshDailyTopProducts() {
        // Redis에서 어제날짜 key로 product-score 조회
        Map<String, Integer> topProductIds = dailySalesProductRepository.findYesterdayRankingProduct();

        // productId 순서로 Product 정보 검색
        List<String> productIds = new ArrayList<>(topProductIds.keySet());
        List<Product> rankingProductResult = productRepository.findAllByIds(productIds);

        // TODO 판매량 추가하여 return하기

        return rankingProductResult;
    }
}
