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

import java.time.LocalDate;
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

    // Product ranking 조회용
    @Query(nativeQuery = true,
    value = "select p.* from order_item o join product p on o.product_id = p.product_id " +
            "where o.created_at >= :startDate group by o.product_id " +
            "order by sum(o.order_item_quantity) DESC LIMIT 5")
    List<Product> findPopularProduct(@Param("startDate")LocalDate startDate);
}
