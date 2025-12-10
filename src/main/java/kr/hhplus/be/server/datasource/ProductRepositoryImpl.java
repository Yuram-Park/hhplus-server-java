package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.ProductRepository;
import kr.hhplus.be.server.application.jpa.ProductJpaRepository;
import kr.hhplus.be.server.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    // 재고 감소용 쓰기 잠금 메서드
    @Override
    public Optional<Product> findByProductIdWithLock(String productId) {
        return productJpaRepository.findByIdWithLock(productId);
    }

    @Override
    public Page<Product> findByPagePerPage(Pageable pageable) {
        return productJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findByProductId(String productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public Product updateByProductId(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public List<Product> findPopularProduct(LocalDate startDate) {
        return productJpaRepository.findPopularProduct(startDate);
    }
}
