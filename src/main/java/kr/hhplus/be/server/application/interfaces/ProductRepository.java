package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.Product;

import java.util.List;

public interface ProductRepository {

    public List<Product> findByPagePerPage(int page, int perPage);
    public Product findByProductId(String productId);
    public Product updateByProductId(String productId, int productInventory);
}
