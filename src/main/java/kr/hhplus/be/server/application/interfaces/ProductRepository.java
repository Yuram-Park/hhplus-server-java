package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {

    public Page<Product> findByPagePerPage(Pageable pageable);
    public Optional<Product> findByProductId(String productId);
    public Product updateByProductId(Product product);
}
