package kr.hhplus.be.server.application.service;

import kr.hhplus.be.server.datasource.ProductRepositoryImpl;
import kr.hhplus.be.server.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return productRepository.findByPagePerPage(page, perPage);
    }

    /**
     * 상품 상세 조회
     * @param productId
     * @return
     */
    public Product getProductDetail(String productId) {
        return productRepository.findByProductId(productId);
    }

    /**
     * 상품 재고 차감
     * @param productId
     * @param reduceCount
     * @return
     */
    public Product reduceProduct(String productId, int reduceCount) {
        Product product = productRepository.findByProductId(productId);
        product.reduceInventory(reduceCount);
        return productRepository.updateByProductId(product.getProductId(), product.getProductInventory());
    }
}
