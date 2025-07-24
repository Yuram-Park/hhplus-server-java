package kr.hhplus.be.server.application.service;

import kr.hhplus.be.server.datasource.ProductRepositoryImpl;
import kr.hhplus.be.server.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepositoryImpl productRepository;

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
    public Product reduceProduct(String productId, int reduceCount) {
        Product product = productRepository.findByProductId(productId).orElseThrow(() -> new NoSuchElementException(productId + " : 해당 상품이 존재하지 않습니다."));
        product.reduceInventory(reduceCount);
        return productRepository.updateByProductId(product);
    }
}
