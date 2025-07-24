package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.ProductRepository;
import kr.hhplus.be.server.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public List<Product> findByPagePerPage(int page, int perPage) {
        return List.of();
    }

    @Override
    public Product findByProductId(String productId) {
        return null;
    }

    @Override
    public Product updateByProductId(String productId, int productInventory) {
        return null;
    }
}
