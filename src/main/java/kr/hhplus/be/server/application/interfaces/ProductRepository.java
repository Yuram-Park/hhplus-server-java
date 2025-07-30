package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {

    Page<Product> findByPagePerPage(Pageable pageable);
    Optional<Product> findByProductId(String productId);
    Product save(Product product);
}
