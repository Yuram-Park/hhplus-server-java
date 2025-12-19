package kr.hhplus.be.server.application.interfaces;

import java.util.Map;

public interface SalesProductRepository {
    void addSalesQuantity(Map<String, Integer> salseData);
}
