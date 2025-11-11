package kr.hhplus.be.server.application.jpa;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ProductJpaRepository extends JpaRepository<Product, String> {

    // 재고 감소용 쓰기 잠금 메서드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.productId = :productId") // table명은 대문자, 필드명도 Entity와 일치해야함
    Optional<Product> findByIdWithLock(@Param("productId") String productId);

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findById(String product_id);

    Product save(Product product);
}
