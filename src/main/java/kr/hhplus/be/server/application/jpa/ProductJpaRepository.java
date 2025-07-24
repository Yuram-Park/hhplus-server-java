package kr.hhplus.be.server.application.jpa;

import kr.hhplus.be.server.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, String> {

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findById(String product_id);

    Product save(Product product);
}
