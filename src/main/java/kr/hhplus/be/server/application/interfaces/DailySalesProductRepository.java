package kr.hhplus.be.server.application.interfaces;

import java.util.List;
import java.util.Map;

public interface DailySalesProductRepository {
    void addSalesQuantity(Map<String, Integer> salseData);

    Map<String, Integer> findYesterdayRankingProduct();

}
